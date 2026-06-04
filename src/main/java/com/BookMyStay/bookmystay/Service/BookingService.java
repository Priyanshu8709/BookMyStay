package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.BookingDto;
import com.BookMyStay.bookmystay.Dto.HotelReportDto;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    List<BookingDto> getMyBookings();

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);
}
