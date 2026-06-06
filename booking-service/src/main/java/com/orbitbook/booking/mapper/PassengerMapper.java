package com.orbitbook.booking.mapper;

import com.orbitbook.booking.dto.passenger.PassengerCreateDTO;
import com.orbitbook.booking.dto.passenger.PassengerResponseDTO;
import com.orbitbook.booking.entity.Passenger;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper {

    public PassengerResponseDTO toResponseDTO(
            Passenger entity) {

        if (entity == null) {
            return null;
        }

        return PassengerResponseDTO.builder()
                .idPassagers(entity.getIdPassagers())
                .createdAt(entity.getCreatedAt())
                .dataNasc(entity.getDataNasc())
                .cpf(entity.getCpf())
                .name(entity.getName())
                .bookingId(entity.getBooking().getIdBookings())
                .build();
    }

    public Passenger toEntity(
            PassengerCreateDTO dto) {

        if (dto == null) {
            return null;
        }

        return Passenger.builder()
                .dataNasc(dto.getDataNasc())
                .cpf(dto.getCpf())
                .name(dto.getName())
                .build();
    }
}