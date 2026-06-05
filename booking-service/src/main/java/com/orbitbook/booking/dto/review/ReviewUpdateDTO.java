package com.orbitbook.booking.dto.review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateDTO {

    private Integer rating;

    private String comment;
}