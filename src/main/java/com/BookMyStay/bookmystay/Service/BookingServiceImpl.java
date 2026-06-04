package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.BookingDto;
import com.BookMyStay.bookmystay.Dto.HotelReportDto;
import com.BookMyStay.bookmystay.Entity.Booking;
import com.BookMyStay.bookmystay.Entity.User;
import com.BookMyStay.bookmystay.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.BookMyStay.bookmystay.Util.AppUtil.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;
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
    public List<BookingDto> getAllBookingsByHotelId(Long hotelId) {
        return List.of();
    }

    @Override
    public HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) {
        return null;
    }


}
