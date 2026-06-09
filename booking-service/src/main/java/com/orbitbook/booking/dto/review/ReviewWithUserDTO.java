package com.orbitbook.booking.dto.review;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewWithUserDTO {
    private Long idReviews;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private Long bookingId;
    private Long userId;
    private String userName;
    private Long destinationId;
}
