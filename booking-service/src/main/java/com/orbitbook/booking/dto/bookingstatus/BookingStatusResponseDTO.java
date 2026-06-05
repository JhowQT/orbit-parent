package com.orbitbook.booking.dto.bookingstatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatusResponseDTO {

    private Long idBookingStatuses;

    private String name;

    private String description;

    private LocalDateTime createdAt;
}