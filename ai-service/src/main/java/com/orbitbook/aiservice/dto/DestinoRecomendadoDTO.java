package com.orbitbook.aiservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinoRecomendadoDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Integer capacity;
    private String imageUrl;
    private String destinationTypeName;
}
