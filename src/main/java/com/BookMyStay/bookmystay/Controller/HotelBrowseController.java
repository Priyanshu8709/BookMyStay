package com.BookMyStay.bookmystay.Controller;

import com.BookMyStay.bookmystay.Dto.HotelInfoDto;
import com.BookMyStay.bookmystay.Dto.HotelInfoRequestDto;
import com.BookMyStay.bookmystay.Dto.HotelPriceResponseDto;
import com.BookMyStay.bookmystay.Dto.HotelSearchRequest;
import com.BookMyStay.bookmystay.Service.HotelService;
import com.BookMyStay.bookmystay.Service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelBrowseController {
    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    @Operation(summary = "Search hotels", tags = {"Browse Hotels"})
    public ResponseEntity<Page<HotelPriceResponseDto>> searchHotels(@RequestParam String city,
                                                                    @RequestParam LocalDate startDate,
                                                                    @RequestParam LocalDate endDate,
                                                                    @RequestParam Integer roomsCount,
                                                                    @RequestParam(defaultValue = "0") Integer page,
                                                                    @RequestParam(defaultValue = "10") Integer size) {
        HotelSearchRequest hotelSearchRequest = new HotelSearchRequest();
        hotelSearchRequest.setCity(city);
        hotelSearchRequest.setStartDate(startDate);
        hotelSearchRequest.setEndDate(endDate);
        hotelSearchRequest.setRoomsCount(roomsCount);
        hotelSearchRequest.setPage(page);
        hotelSearchRequest.setSize(size);

        var hotelPage = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(hotelPage);
    }

    @GetMapping("/{hotelId}/info")
    @Operation(summary = "Get a hotel info by hotelId", tags = {"Browse Hotels"})
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId,
                                                     @RequestParam LocalDate startDate,
                                                     @RequestParam LocalDate endDate,
                                                     @RequestParam Long roomsCount) {
        HotelInfoRequestDto hotelInfoRequestDto = new HotelInfoRequestDto(startDate, endDate, roomsCount);
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId, hotelInfoRequestDto));
    }

}
