package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.GuestDto;

import java.util.List;

public interface GuestService {
    List<GuestDto> getAllGuests();
    void updateGuest(Long guestId, GuestDto guestDto);

    void deleteGuest(Long guestId);

    GuestDto addGuest(GuestDto guestDto);
}
