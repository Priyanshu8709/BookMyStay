package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.BookingDto;

import java.util.List;

public interface BookingService {
    List<BookingDto> getMyBookings();
}
