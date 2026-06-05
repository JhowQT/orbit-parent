package com.orbitbook.booking.dto.destination;

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
public class DestinationResponseDTO {

    private Long idDestinations;

    private String name;

    private String description;

    private Integer distanceKm;

    private Integer basePrice;

    private Integer capacity;

    private String imageUrl;

    private LocalDateTime createdAt;

    private Long destinationTypeId;

    private String destinationTypeName;
}