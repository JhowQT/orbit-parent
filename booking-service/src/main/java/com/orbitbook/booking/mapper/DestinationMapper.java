package com.orbitbook.booking.mapper;

import com.orbitbook.booking.dto.destination.DestinationCreateDTO;
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

    public Destination toEntity(
            DestinationCreateDTO dto) {

        if (dto == null) {
            return null;
        }

        return Destination.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .distanceKm(dto.getDistanceKm())
                .basePrice(dto.getBasePrice())
                .capacity(dto.getCapacity())
                .imageUrl(dto.getImageUrl())
                .build();
    }
}