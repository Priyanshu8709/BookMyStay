package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.BookingDto;
import com.BookMyStay.bookmystay.Dto.BookingRequest;
import com.BookMyStay.bookmystay.Dto.HotelReportDto;
import com.BookMyStay.bookmystay.Entity.Enum.BookingStatus;
import com.stripe.model.Event;

import java.time.LocalDate;
import java.util.List;


public interface BookingService {
    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<Long> guestIdList);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    BookingDto confirmPayment(Long bookingId, String paymentSessionId);

    void cancelBooking(Long bookingId);

    BookingStatus getBookingStatus(Long bookingId);

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();
}
