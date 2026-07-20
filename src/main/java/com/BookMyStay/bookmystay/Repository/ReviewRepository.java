package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Hotel;
import com.BookMyStay.bookmystay.Entity.Review;
import com.BookMyStay.bookmystay.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHotelIdOrderByCreatedAtDesc(Long hotelId);

    Optional<Review> findByHotelAndUser(Hotel hotel, User user);
}
