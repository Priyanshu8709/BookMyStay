package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}