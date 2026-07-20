package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createRoom(Long hotelId, RoomDto roomDto);

    List<RoomDto> getRoomsByHotel(Long hotelId);

    RoomDto updateRoom(Long hotelId, Long roomId, RoomDto roomDto);

    void deleteRoom(Long hotelId, Long roomId);
}
