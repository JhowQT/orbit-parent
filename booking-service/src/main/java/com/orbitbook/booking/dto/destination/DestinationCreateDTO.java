package com.orbitbook.booking.dto.destination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationCreateDTO {

    private String name;

    private String description;

    private Integer distanceKm;

    private Integer basePrice;

    private Integer capacity;

    private String imageUrl;

    private Long destinationTypeId;
}