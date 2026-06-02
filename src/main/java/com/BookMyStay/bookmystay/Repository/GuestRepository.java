package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Guest;
import com.BookMyStay.bookmystay.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByUser(User user);
}