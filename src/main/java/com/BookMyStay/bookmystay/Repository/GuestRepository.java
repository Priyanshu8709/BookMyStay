package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}