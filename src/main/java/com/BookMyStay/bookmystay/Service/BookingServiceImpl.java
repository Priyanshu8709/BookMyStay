package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.BookingDto;
import com.BookMyStay.bookmystay.Dto.BookingRequest;
import com.BookMyStay.bookmystay.Dto.HotelReportDto;
import com.BookMyStay.bookmystay.Entity.*;
import com.BookMyStay.bookmystay.Entity.Enum.BookingStatus;
import com.BookMyStay.bookmystay.Entity.User;
import com.BookMyStay.bookmystay.Exception.ResourceNotFoundException;
import com.BookMyStay.bookmystay.Repository.*;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.BookMyStay.bookmystay.Util.AppUtil.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final GuestRepository guestRepository;
    @Override
    public List<BookingDto> getMyBookings() {
        User user = getCurrentUser();
        log.info("Fetching bookings for user with id: {}", user.getId());
        List<Booking> bookings = bookingRepository.findByUser(user);
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingRequest bookingRequest) {
        validateBookingRequest(bookingRequest);
        User user = getCurrentUser();

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + bookingRequest.getHotelId()));
        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + bookingRequest.getRoomId()));
        if (!Objects.equals(room.getHotel().getId(), hotel.getId())) {
            throw new IllegalArgumentException("Room does not belong to selected hotel");
        }
        if (!Boolean.TRUE.equals(hotel.getActive())) {
            throw new IllegalArgumentException("Hotel is not active");
        }

        long dateCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate()) + 1;
        List<Inventory> inventories = inventoryRepository.findAndLockAvailableInventory(
                room.getId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomsCount()
        );
        if (inventories.size() != dateCount) {
            throw new IllegalArgumentException("Requested rooms are not available for the selected dates");
        }

        BigDecimal amount = inventories.stream()
                .map(inventory -> inventory.getPrice()
                        .multiply(inventory.getSurgeFactor())
                        .multiply(BigDecimal.valueOf(bookingRequest.getRoomsCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        inventoryRepository.initBooking(room.getId(), bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());

        Booking booking = Booking.builder()
                .hotel(hotel)
                .room(room)
                .user(user)
                .roomsCount(bookingRequest.getRoomsCount())
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .bookingStatus(BookingStatus.RESERVED)
                .amount(amount)
                .build();
        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<Long> guestIdList) {
        Booking booking = getCurrentUserBooking(bookingId);
        if (booking.getBookingStatus() != BookingStatus.RESERVED &&
                booking.getBookingStatus() != BookingStatus.GUESTS_ADDED) {
            throw new IllegalStateException("Guests can only be added before payment");
        }

        User user = getCurrentUser();
        List<Guest> guests = guestRepository.findAllById(guestIdList);
        if (guests.size() != guestIdList.size()) {
            throw new ResourceNotFoundException("One or more guests were not found");
        }
        boolean hasOtherUserGuest = guests.stream()
                .anyMatch(guest -> guest.getUser() == null || !Objects.equals(guest.getUser().getId(), user.getId()));
        if (hasOtherUserGuest) {
            throw new AccessDeniedException("One or more guests do not belong to current user");
        }

        booking.setGuests(new HashSet<>(guests));
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    @Transactional
    public String initiatePayments(Long bookingId) {
        Booking booking = getCurrentUserBooking(bookingId);
        if (booking.getBookingStatus() != BookingStatus.RESERVED &&
                booking.getBookingStatus() != BookingStatus.GUESTS_ADDED &&
                booking.getBookingStatus() != BookingStatus.PAYMENTS_PENDING) {
            throw new IllegalStateException("Payment cannot be initiated for booking status: " + booking.getBookingStatus());
        }

        if (booking.getPaymentSessionId() == null) {
            booking.setPaymentSessionId("local_" + UUID.randomUUID());
        }
        booking.setBookingStatus(BookingStatus.PAYMENTS_PENDING);
        bookingRepository.save(booking);
        return "/api/v1/bookings/" + booking.getId() + "/payments/confirm?sessionId=" + booking.getPaymentSessionId();
    }

    @Override
    @Transactional
    public void capturePayment(Event event) {
        if (!"checkout.session.completed".equals(event.getType())) {
            return;
        }

        event.getDataObjectDeserializer().getObject().ifPresent(stripeObject -> {
            if (stripeObject instanceof Session session) {
                bookingRepository.findByPaymentSessionId(session.getId())
                        .ifPresent(booking -> confirmBookingPayment(booking, session.getId()));
            }
        });
    }

    @Override
    @Transactional
    public BookingDto confirmPayment(Long bookingId, String paymentSessionId) {
        Booking booking = getCurrentUserBooking(bookingId);
        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            return modelMapper.map(booking, BookingDto.class);
        }
        if (booking.getBookingStatus() != BookingStatus.PAYMENTS_PENDING) {
            throw new IllegalStateException("Booking is not waiting for payment");
        }
        if (!Objects.equals(booking.getPaymentSessionId(), paymentSessionId)) {
            throw new IllegalArgumentException("Invalid payment session");
        }

        return confirmBookingPayment(booking, paymentSessionId);
    }

    private BookingDto confirmBookingPayment(Booking booking, String paymentSessionId) {
        if (!Objects.equals(booking.getPaymentSessionId(), paymentSessionId)) {
            throw new IllegalArgumentException("Invalid payment session");
        }
        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomsCount());
        inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomsCount());
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = getCurrentUserBooking(bookingId);
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            return;
        }
        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomsCount());
        } else if (booking.getBookingStatus() == BookingStatus.RESERVED ||
                booking.getBookingStatus() == BookingStatus.GUESTS_ADDED ||
                booking.getBookingStatus() == BookingStatus.PAYMENTS_PENDING) {
            inventoryRepository.releaseReservation(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomsCount());
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

    }

    @Override
    public BookingStatus getBookingStatus(Long bookingId) {
        return getCurrentUserBooking(bookingId).getBookingStatus();
    }

    @Override
    public List<BookingDto> getAllBookingsByHotelId(Long hotelId) {
        ensureOwnedHotel(hotelId);
        return bookingRepository.findByHotelId(hotelId).stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) {
        ensureOwnedHotel(hotelId);
        List<Booking> bookings = bookingRepository.findByHotelId(hotelId).stream()
                .filter(booking -> booking.getBookingStatus() == BookingStatus.CONFIRMED)
                .filter(booking -> !booking.getCheckInDate().isBefore(startDate) && !booking.getCheckInDate().isAfter(endDate))
                .toList();
        BigDecimal totalRevenue = bookings.stream()
                .map(Booking::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avgRevenue = bookings.isEmpty()
                ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(bookings.size()), 2, java.math.RoundingMode.HALF_UP);
        return new HotelReportDto((long) bookings.size(), totalRevenue, avgRevenue);
    }

    private Booking getCurrentUserBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        User user = getCurrentUser();
        if (!Objects.equals(booking.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("You are not the owner of booking with id: " + bookingId);
        }
        return booking;
    }

    private void ensureOwnedHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        User user = getCurrentUser();
        if (hotel.getOwner() == null || !Objects.equals(hotel.getOwner().getId(), user.getId())) {
            throw new AccessDeniedException("You are not the owner of hotel with id: " + hotelId);
        }
    }

    private void validateBookingRequest(BookingRequest bookingRequest) {
        if (bookingRequest.getHotelId() == null || bookingRequest.getRoomId() == null ||
                bookingRequest.getCheckInDate() == null || bookingRequest.getCheckOutDate() == null ||
                bookingRequest.getRoomsCount() == null) {
            throw new IllegalArgumentException("Hotel, room, dates and rooms count are required");
        }
        if (bookingRequest.getRoomsCount() <= 0) {
            throw new IllegalArgumentException("Rooms count must be greater than zero");
        }
        if (bookingRequest.getCheckInDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date cannot be before check-in date");
        }
    }

}
