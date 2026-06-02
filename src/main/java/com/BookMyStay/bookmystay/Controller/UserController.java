package com.BookMyStay.bookmystay.Controller;

import com.BookMyStay.bookmystay.Dto.BookingDto;
import com.BookMyStay.bookmystay.Dto.GuestDto;
import com.BookMyStay.bookmystay.Dto.ProfileUpdateRequestDto;
import com.BookMyStay.bookmystay.Dto.UserDto;
import com.BookMyStay.bookmystay.Service.BookingService;
import com.BookMyStay.bookmystay.Service.GuestService;
import com.BookMyStay.bookmystay.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final GuestService guestService;
    private final BookingService bookingService;

    @PatchMapping("/profile")
    @Operation(summary = "update the user profile", tags = {"Profile"})
    public ResponseEntity<Void> updateUser(@RequestBody ProfileUpdateRequestDto profileUpdateRequestDto) {
        userService.updateProfile(profileUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    @Operation(summary = "Get profile",tags={"Profile"})
    public ResponseEntity<UserDto> getProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @GetMapping("/guests")
    @Operation(summary = "Get All guest", tags={"Booking Guest"})
    public ResponseEntity<List<GuestDto>> getGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @PostMapping("/guest")
    @Operation(summary = "Add a new Guest in List",tags = {"Booking Guest"})
    public ResponseEntity<GuestDto> addGuest(@RequestBody GuestDto guestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.addGuest(guestDto));
    }

    @PutMapping("/guest/{Guest_id}")
    @Operation(summary = "Update the guest",tags = {"Booking Guest"})
    public ResponseEntity<Void>  updateGuest(@RequestBody GuestDto guestDto, @PathVariable Long Guest_id){
        guestService.updateGuest(Guest_id,guestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/guest/{guest_id}")
    @Operation(summary = "Delete the guest",tags = {"Booking Guest"})
    public ResponseEntity<Void>  deleteGuest(@PathVariable Long guest_id){
        guestService.deleteGuest(guest_id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myBookings")
    @Operation(summary = "Get all my previous bookings", tags = {"Profile"})
    public ResponseEntity<List<BookingDto>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }
}
