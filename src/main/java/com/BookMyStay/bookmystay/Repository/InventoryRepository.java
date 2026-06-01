package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}