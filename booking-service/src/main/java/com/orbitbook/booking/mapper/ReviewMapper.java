package com.orbitbook.booking.mapper;

import com.orbitbook.booking.dto.review.ReviewCreateDTO;
import com.orbitbook.booking.dto.review.ReviewResponseDTO;
import com.orbitbook.booking.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponseDTO toResponseDTO(
            Review entity) {

        if (entity == null) {
            return null;
        }

        return ReviewResponseDTO.builder()
                .idReviews(entity.getIdReviews())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .createdAt(entity.getCreatedAt())
                .bookingId(entity.getBooking().getIdBookings())
                .build();
    }

    public Review toEntity(
            ReviewCreateDTO dto) {

        if (dto == null) {
            return null;
        }

        return Review.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }
}