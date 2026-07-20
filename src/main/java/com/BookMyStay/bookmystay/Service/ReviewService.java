package com.BookMyStay.bookmystay.Service;

import com.BookMyStay.bookmystay.Dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    ReviewDto addOrUpdateReview(Long hotelId, ReviewDto reviewDto);

    List<ReviewDto> getReviewsByHotel(Long hotelId);
}
