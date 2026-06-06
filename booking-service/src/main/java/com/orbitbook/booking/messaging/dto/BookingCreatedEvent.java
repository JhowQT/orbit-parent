package com.orbitbook.booking.messaging.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreatedEvent {

    private Long bookingId;

    private Long userId;

    private Long destinationId;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;
}