package com.orbitbook.booking.dto.booking;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreateDTO {

    private LocalDateTime departureDate;

    private LocalDateTime returnDate;

    private Integer numPassengers;

    private Long userId;

    private Long destinationId;

    private Long aiRecommendationId;
}