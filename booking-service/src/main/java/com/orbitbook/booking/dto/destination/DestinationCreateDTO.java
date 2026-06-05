package com.orbitbook.booking.dto.destination;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationCreateDTO {

    private String name;

    private String description;

    private Integer distanceKm;

    private BigDecimal basePrice;

    private Integer capacity;

    private String imageUrl;

    private Long destinationTypeId;
}