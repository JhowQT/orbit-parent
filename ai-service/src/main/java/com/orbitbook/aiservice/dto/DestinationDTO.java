package com.orbitbook.aiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationDTO {

    private Long idDestinations;

    private String name;

    private String description;

    private Integer distanceKm;

    private BigDecimal basePrice;

    private Integer capacity;

    private String imageUrl;

    private LocalDateTime createdAt;

    private Long destinationTypeId;

    private String destinationTypeName;
}