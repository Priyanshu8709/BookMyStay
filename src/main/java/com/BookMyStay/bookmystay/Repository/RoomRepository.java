package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}