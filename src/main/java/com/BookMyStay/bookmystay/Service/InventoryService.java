package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.HotelPriceResponseDto;
import com.BookMyStay.bookmystay.Dto.InventoryDto;
import com.BookMyStay.bookmystay.Dto.UpdateInventoryRequestDto;
import com.BookMyStay.bookmystay.Entity.Room;
import com.BookMyStay.bookmystay.Dto.HotelSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceResponseDto> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);

}
