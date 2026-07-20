package com.BookMyStay.bookmystay.Controller;

import com.BookMyStay.bookmystay.Dto.RoomDto;
import com.BookMyStay.bookmystay.Service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotel/{hotelId}/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "Add a room to a hotel", tags = {"Admin Room"})
    public ResponseEntity<RoomDto> createRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(hotelId, roomDto));
    }

    @GetMapping
    @Operation(summary = "Get rooms of a hotel", tags = {"Admin Room"})
    public ResponseEntity<List<RoomDto>> getRooms(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "Update a room", tags = {"Admin Room"})
    public ResponseEntity<RoomDto> updateRoom(@PathVariable Long hotelId,
                                              @PathVariable Long roomId,
                                              @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.updateRoom(hotelId, roomId, roomDto));
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "Delete a room", tags = {"Admin Room"})
    public ResponseEntity<Void> deleteRoom(@PathVariable Long hotelId, @PathVariable Long roomId) {
        roomService.deleteRoom(hotelId, roomId);
        return ResponseEntity.noContent().build();
    }
}
