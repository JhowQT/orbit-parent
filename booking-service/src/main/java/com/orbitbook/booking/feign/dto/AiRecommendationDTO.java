package com.orbitbook.booking.feign.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRecommendationDTO {

    private Long idRecommendation;

    private Long userId;

    private Long destinationId;

    private String destinationName;

    private BigDecimal estimatedPrice;

    private String recommendationReason;

    private LocalDateTime createdAt;
}