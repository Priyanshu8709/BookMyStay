package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.ReviewDto;
import com.BookMyStay.bookmystay.Entity.Enum.BookingStatus;
import com.BookMyStay.bookmystay.Entity.Hotel;
import com.BookMyStay.bookmystay.Entity.Review;
import com.BookMyStay.bookmystay.Entity.User;
import com.BookMyStay.bookmystay.Exception.ResourceNotFoundException;
import com.BookMyStay.bookmystay.Repository.BookingRepository;
import com.BookMyStay.bookmystay.Repository.HotelRepository;
import com.BookMyStay.bookmystay.Repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.BookMyStay.bookmystay.Util.AppUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ReviewDto addOrUpdateReview(Long hotelId, ReviewDto reviewDto) {
        if (reviewDto.getRating() == null || reviewDto.getRating() < 1 || reviewDto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        User user = getCurrentUser();
        boolean hasConfirmedBooking = bookingRepository.existsByHotelIdAndUserAndBookingStatus(
                hotelId, user, BookingStatus.CONFIRMED);
        if (!hasConfirmedBooking) {
            throw new IllegalStateException("Only users with a confirmed booking can review this hotel");
        }

        Review review = reviewRepository.findByHotelAndUser(hotel, user).orElseGet(Review::new);
        review.setHotel(hotel);
        review.setUser(user);
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        return toDto(reviewRepository.save(review));
    }

    @Override
    public List<ReviewDto> getReviewsByHotel(Long hotelId) {
        return reviewRepository.findByHotelIdOrderByCreatedAtDesc(hotelId).stream()
                .map(this::toDto)
                .toList();
    }

    private ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setHotelId(review.getHotel().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }
}
