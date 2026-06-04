package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<RoomPriceDto> findRoomAveragePrice(Long hotelId, LocalDate startDate, LocalDate endDate, Long roomsCount, long daysCount);
}