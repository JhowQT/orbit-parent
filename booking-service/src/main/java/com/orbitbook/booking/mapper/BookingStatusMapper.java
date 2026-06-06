package com.orbitbook.booking.mapper;

import com.orbitbook.booking.dto.bookingstatus.BookingStatusCreateDTO;
import com.orbitbook.booking.dto.bookingstatus.BookingStatusResponseDTO;
import com.orbitbook.booking.entity.BookingStatus;
import org.springframework.stereotype.Component;

@Component
public class BookingStatusMapper {

    public BookingStatusResponseDTO toResponseDTO(
            BookingStatus entity) {

        if (entity == null) {
            return null;
        }

        return BookingStatusResponseDTO.builder()
                .idBookingStatuses(entity.getIdBookingStatuses())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public BookingStatus toEntity(
            BookingStatusCreateDTO dto) {

        if (dto == null) {
            return null;
        }

        return BookingStatus.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
}