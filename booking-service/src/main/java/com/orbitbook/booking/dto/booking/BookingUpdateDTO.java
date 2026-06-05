package com.orbitbook.booking.dto.booking;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingUpdateDTO {

    private LocalDateTime departureDate;

    private LocalDateTime returnDate;

    private Integer numPassengers;

    private Long destinationId;

    private Long bookingStatusId;
}