package com.orbitbook.booking.dto.booking;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {

    private Long idBookings;

    private LocalDateTime departureDate;

    private LocalDateTime returnDate;

    private BigDecimal totalPrice;

    private Integer numPassengers;

    private LocalDateTime createdAt;

    private Long userId;

    private Long aiRecommendationId;

    private Long destinationId;

    private String destinationName;

    private Long bookingStatusId;

    private String bookingStatusName;
}