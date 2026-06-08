package com.orbitbook.booking.dto.destination;

import lombok.*;

import java.math.BigDecimal;
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

    private BigDecimal basePrice;

    private Integer capacity;

    private String imageUrl;

    private LocalDateTime createdAt;

    private Long destinationTypeId;

    private String destinationTypeName;

    private AvaliacaoResumoDTO avaliacao;
}