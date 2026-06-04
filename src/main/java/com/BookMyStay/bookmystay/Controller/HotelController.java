package com.BookMyStay.bookmystay.Controller;

import com.BookMyStay.bookmystay.Dto.BookingDto;
import com.BookMyStay.bookmystay.Dto.HotelDto;
import com.BookMyStay.bookmystay.Dto.HotelReportDto;
import com.BookMyStay.bookmystay.Service.BookingService;
import com.BookMyStay.bookmystay.Service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/hotel")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
    private final HotelService hotelService;
    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Add a new hotel",tags = {"Admin Hotel"})
    public ResponseEntity<HotelDto> addHotel(@RequestBody HotelDto hotelDto) {
        log.info("Add hotel");
        HotelDto hotel =hotelService.createNewHotel(hotelDto);
        return new  ResponseEntity<>(hotel, HttpStatus.CREATED);
    }
    @GetMapping("/{hotelId}")
    @Operation(summary = "Getting hotel by id",tags = {"Admin Hotel"})
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId) {
        log.info("Get hotel by id");
        HotelDto hotelDto=hotelService.getHotelById(hotelId);
        return new  ResponseEntity<>(hotelDto, HttpStatus.OK);
    }
    @PutMapping("/{hotelId}")
    @Operation(summary = "Update a hotel", tags = {"Admin Hotel"})
    public ResponseEntity<HotelDto> updateHotel(@PathVariable Long hotelId, @RequestBody HotelDto hotelDto) {
        log.info("Update hotel");
        HotelDto hotel =hotelService.updateHotelById(hotelId,hotelDto);
        return new  ResponseEntity<>(hotel, HttpStatus.OK);
    }
    @DeleteMapping("/{hotelId}")
    @Operation(summary = "Deleting Hotel By id",tags = {"Admin Hotel"})
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId) {
        log.info("Delete hotel by id");
        hotelService.deleteHotelByID(hotelId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{hotelId}/active")
    @Operation(summary = "Activate a Hotel",tags = {"Admin Hotel"})
    public ResponseEntity<Void>  activateHotel(@PathVariable Long hotelId) {
        log.info("Activate hotel");
        hotelService.activateHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{hotelId}/deactive")
    @Operation(summary = "Deactivate a Hotel",tags = {"Admin Hotel"})
    public ResponseEntity<Void>  activateHotel(@PathVariable Long hotelId) {
        log.info("Deactivate hotel");
        hotelService.deactivateHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    @Operation(summary = "Get all hotel owed by admin",tags = {"Hotel Admin"})
    public ResponseEntity<List<HotelDto>> getAllHotelByAdmin() {
        log.info("Get all hotel owed by admin");
        return ResponseEntity.ok(hotelService.getAllHotel());
    }
    @GetMapping("/{hotelId}/bookings")
    @Operation(summary = "Get all hotel bookings",tags = {"Hotel Admin"})
    public ResponseEntity<List<BookingDto>> getAllHotelBookings(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }
    @GetMapping("/{hotelId}/reports")
    @Operation(summary = "Generate a bookings report of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId,
                                                         @RequestParam(required = false) LocalDate startDate,
                                                         @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }
}
