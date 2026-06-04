package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Hotel;
import com.BookMyStay.bookmystay.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByOwner(User user);
}