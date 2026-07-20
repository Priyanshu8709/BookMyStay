package com.BookMyStay.bookmystay.Controller;

import com.BookMyStay.bookmystay.Dto.ReviewDto;
import com.BookMyStay.bookmystay.Service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels/{hotelId}/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Add or update a hotel review", tags = {"Reviews"})
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long hotelId, @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addOrUpdateReview(hotelId, reviewDto));
    }

    @GetMapping
    @Operation(summary = "Get hotel reviews", tags = {"Reviews"})
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long hotelId) {
        return ResponseEntity.ok(reviewService.getReviewsByHotel(hotelId));
    }
}
