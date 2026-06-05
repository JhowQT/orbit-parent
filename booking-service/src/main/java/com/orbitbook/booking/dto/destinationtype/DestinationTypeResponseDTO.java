package com.orbitbook.booking.dto.destinationtype;

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
public class DestinationTypeResponseDTO {

    private Long idDestinationTypes;

    private String name;

    private String description;

    private LocalDateTime createdAt;
}