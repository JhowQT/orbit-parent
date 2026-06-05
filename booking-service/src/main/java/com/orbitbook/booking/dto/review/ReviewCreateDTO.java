package com.orbitbook.booking.dto.review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateDTO {

    private Integer rating;

    private String comment;

    private Long bookingId;
}