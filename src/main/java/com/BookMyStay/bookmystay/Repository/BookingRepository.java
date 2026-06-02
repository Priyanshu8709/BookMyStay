package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Booking;
import com.BookMyStay.bookmystay.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}
