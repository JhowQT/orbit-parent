package com.orbitbook.booking.mapper;

import com.orbitbook.booking.dto.destinationtype.DestinationTypeResponseDTO;
import com.orbitbook.booking.entity.DestinationType;
import org.springframework.stereotype.Component;

@Component
public class DestinationTypeMapper {

    public DestinationTypeResponseDTO toResponseDTO(
            DestinationType entity) {

        if (entity == null) {
            return null;
        }

        return DestinationTypeResponseDTO.builder()
                .idDestinationTypes(entity.getIdDestinationTypes())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}