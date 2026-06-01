package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}