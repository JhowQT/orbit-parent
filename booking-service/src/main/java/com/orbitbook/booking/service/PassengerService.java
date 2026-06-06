package com.orbitbook.booking.service;

import com.orbitbook.booking.dto.passenger.PassengerCreateDTO;
import com.orbitbook.booking.dto.passenger.PassengerResponseDTO;
import com.orbitbook.booking.dto.passenger.PassengerUpdateDTO;
import com.orbitbook.booking.entity.Booking;
import com.orbitbook.booking.entity.Passenger;
import com.orbitbook.booking.exception.ResourceNotFoundException;
import com.orbitbook.booking.mapper.PassengerMapper;
import com.orbitbook.booking.repository.BookingRepository;
import com.orbitbook.booking.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerService {

    private final PassengerRepository repository;

    private final BookingRepository bookingRepository;

    private final PassengerMapper mapper;

    public PassengerResponseDTO create(
            PassengerCreateDTO dto) {

        if (dto.getCpf() == null
                || dto.getCpf().isBlank()) {

            throw new IllegalArgumentException(
                    "CPF é obrigatório."
            );
        }

        if (repository.existsByCpf(
                dto.getCpf())) {

            throw new IllegalArgumentException(
                    "Já existe um passageiro cadastrado com este CPF."
            );
        }

        Booking booking =
                bookingRepository.findById(
                                dto.getBookingId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reserva não encontrada. ID: "
                                                + dto.getBookingId()
                                )
                        );

        Passenger passenger =
                mapper.toEntity(dto);

        passenger.setBooking(
                booking
        );

        passenger.setCreatedAt(
                LocalDateTime.now()
        );

        Passenger saved =
                repository.save(passenger);

        return mapper.toResponseDTO(
                saved
        );
    }

    @Transactional(readOnly = true)
    public PassengerResponseDTO findById(
            Long id) {

        Passenger passenger =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Passageiro não encontrado. ID: "
                                                + id
                                )
                        );

        return mapper.toResponseDTO(
                passenger
        );
    }

    @Transactional(readOnly = true)
    public List<PassengerResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public PassengerResponseDTO update(
            Long id,
            PassengerUpdateDTO dto) {

        Passenger passenger =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Passageiro não encontrado. ID: "
                                                + id
                                )
                        );

        if (dto.getCpf() == null
                || dto.getCpf().isBlank()) {

            throw new IllegalArgumentException(
                    "CPF é obrigatório."
            );
        }

        repository.findByCpf(
                        dto.getCpf()
                )
                .ifPresent(existing -> {

                    if (!existing.getIdPassagers()
                            .equals(
                                    passenger.getIdPassagers()
                            )) {

                        throw new IllegalArgumentException(
                                "Já existe um passageiro cadastrado com este CPF."
                        );
                    }
                });

        passenger.setName(
                dto.getName()
        );

        passenger.setCpf(
                dto.getCpf()
        );

        passenger.setDataNasc(
                dto.getDataNasc()
        );

        Passenger updated =
                repository.save(passenger);

        return mapper.toResponseDTO(
                updated
        );
    }

    public void delete(
            Long id) {

        Passenger passenger =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Passageiro não encontrado. ID: "
                                                + id
                                )
                        );

        repository.delete(
                passenger
        );
    }
}