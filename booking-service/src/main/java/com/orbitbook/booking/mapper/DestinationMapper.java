package com.orbitbook.booking.mapper;

import com.orbitbook.booking.dto.destination.DestinationResponseDTO;
import com.orbitbook.booking.entity.Destination;
import org.springframework.stereotype.Component;

@Component
public class DestinationMapper {

    public DestinationResponseDTO toResponseDTO(
            Destination entity) {

        if (entity == null) {
            return null;
        }

        return DestinationResponseDTO.builder()
                .idDestinations(entity.getIdDestinations())
                .name(entity.getName())
                .description(entity.getDescription())
                .distanceKm(entity.getDistanceKm())
                .basePrice(entity.getBasePrice())
                .capacity(entity.getCapacity())
                .imageUrl(entity.getImageUrl())
                .createdAt(entity.getCreatedAt())
                .destinationTypeId(
                        entity.getDestinationType()
                                .getIdDestinationTypes()
                )
                .destinationTypeName(
                        entity.getDestinationType()
                                .getName()
                )
                .build();
    }
}