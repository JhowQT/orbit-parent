package com.orbitbook.booking.service;

import com.orbitbook.booking.dto.bookingstatus.BookingStatusCreateDTO;
import com.orbitbook.booking.dto.bookingstatus.BookingStatusResponseDTO;
import com.orbitbook.booking.dto.bookingstatus.BookingStatusUpdateDTO;
import com.orbitbook.booking.entity.BookingStatus;
import com.orbitbook.booking.exception.ResourceNotFoundException;
import com.orbitbook.booking.mapper.BookingStatusMapper;
import com.orbitbook.booking.repository.BookingStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingStatusService {

    private final BookingStatusRepository repository;
    private final BookingStatusMapper mapper;

    public BookingStatusResponseDTO create(
            BookingStatusCreateDTO dto) {

        BookingStatus bookingStatus =
                mapper.toEntity(dto);

        bookingStatus.setCreatedAt(
                LocalDateTime.now()
        );

        BookingStatus saved =
                repository.save(bookingStatus);

        return mapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public BookingStatusResponseDTO findById(
            Long id) {

        BookingStatus bookingStatus =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Status da reserva não encontrado. ID: "
                                                + id
                                )
                        );

        return mapper.toResponseDTO(
                bookingStatus
        );
    }

    @Transactional(readOnly = true)
    public List<BookingStatusResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public BookingStatusResponseDTO update(
            Long id,
            BookingStatusUpdateDTO dto) {

        BookingStatus bookingStatus =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Status da reserva não encontrado. ID: "
                                                + id
                                )
                        );

        bookingStatus.setName(
                dto.getName()
        );

        bookingStatus.setDescription(
                dto.getDescription()
        );

        BookingStatus updated =
                repository.save(bookingStatus);

        return mapper.toResponseDTO(updated);
    }

    public void delete(Long id) {

        BookingStatus bookingStatus =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Status da reserva não encontrado. ID: "
                                                + id
                                )
                        );

        repository.delete(bookingStatus);
    }
}