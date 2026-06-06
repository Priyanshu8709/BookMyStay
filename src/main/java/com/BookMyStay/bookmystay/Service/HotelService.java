package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.HotelDto;
import com.BookMyStay.bookmystay.Dto.HotelInfoDto;
import com.BookMyStay.bookmystay.Dto.HotelInfoRequestDto;

import java.util.List;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long hotelId);

    HotelDto updateHotelById(Long hotelId, HotelDto hotelDto);

    void deleteHotelByID(Long hotelId);

    void activateHotelById(Long hotelId);

    List<HotelDto> getAllHotel();

    HotelInfoDto getHotelInfoById(Long hotelId, HotelInfoRequestDto hotelInfoRequestDto);
}
