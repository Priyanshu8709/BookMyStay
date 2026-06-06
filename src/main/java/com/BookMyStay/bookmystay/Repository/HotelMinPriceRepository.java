package com.BookMyStay.bookmystay.Repository;

import com.BookMyStay.bookmystay.Dto.HotelPriceDto;
import com.BookMyStay.bookmystay.Entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice, Long> {
    @Query(
            value = """
                    SELECT new com.BookMyStay.bookmystay.Dto.HotelPriceDto(hmp.hotel, AVG(hmp.price))
                    FROM HotelMinPrice hmp
                    WHERE hmp.hotel.city = :city
                      AND hmp.date BETWEEN :startDate AND :endDate
                      AND SIZE(hmp.hotel.rooms) >= :roomsCount
                    GROUP BY hmp.hotel
                    HAVING COUNT(DISTINCT hmp.date) = :dateCount
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT hmp.hotel.id)
                    FROM HotelMinPrice hmp
                    WHERE hmp.hotel.city = :city
                      AND hmp.date BETWEEN :startDate AND :endDate
                      AND SIZE(hmp.hotel.rooms) >= :roomsCount
                    """
    )
    Page<HotelPriceDto> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount,
            @Param("dateCount") long dateCount,
            Pageable pageable
    );
}
