package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Entity.Room;

public interface InventoryService {
    void deleteAllInventories(Room room);

    void initializeRoomForAYear(Room room);

    void deinitializeRoomForAYear(Room room);
}
