package com.orbitbook.aiservice.feign;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponseDTO {

    private Long idBookings;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private BigDecimal totalPrice;
    private Integer numPassengers;
    private LocalDateTime createdAt;
    private Long userId;
    private String destinationName;
    private String bookingStatusName;
}
