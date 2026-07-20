package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Booking;
import com.BookMyStay.bookmystay.Entity.User;
import com.BookMyStay.bookmystay.Entity.Enum.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);

    List<Booking> findByHotelId(Long hotelId);

    Optional<Booking> findByPaymentSessionId(String paymentSessionId);

    boolean existsByHotelIdAndUserAndBookingStatus(Long hotelId, User user, BookingStatus bookingStatus);
}
