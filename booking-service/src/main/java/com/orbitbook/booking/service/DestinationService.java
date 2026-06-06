package com.orbitbook.booking.service;

import com.orbitbook.booking.dto.destination.DestinationCreateDTO;
import com.orbitbook.booking.dto.destination.DestinationResponseDTO;
import com.orbitbook.booking.dto.destination.DestinationUpdateDTO;
import com.orbitbook.booking.entity.Destination;
import com.orbitbook.booking.entity.DestinationType;
import com.orbitbook.booking.exception.ResourceNotFoundException;
import com.orbitbook.booking.mapper.DestinationMapper;
import com.orbitbook.booking.repository.DestinationRepository;
import com.orbitbook.booking.repository.DestinationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DestinationService {

    private final DestinationRepository repository;
    private final DestinationTypeRepository destinationTypeRepository;
    private final DestinationMapper mapper;

    public DestinationResponseDTO create(
            DestinationCreateDTO dto) {

        DestinationType destinationType =
                destinationTypeRepository
                        .findById(dto.getDestinationTypeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Tipo de destino não encontrado. ID: "
                                                + dto.getDestinationTypeId()
                                )
                        );

        Destination destination =
                mapper.toEntity(dto);

        destination.setDestinationType(
                destinationType
        );

        destination.setCreatedAt(
                LocalDateTime.now()
        );

        Destination saved =
                repository.save(destination);

        return mapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public DestinationResponseDTO findById(
            Long id) {

        Destination destination =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Destino não encontrado. ID: "
                                                + id
                                )
                        );

        return mapper.toResponseDTO(
                destination
        );
    }

    @Transactional(readOnly = true)
    public List<DestinationResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public DestinationResponseDTO update(
            Long id,
            DestinationUpdateDTO dto) {

        Destination destination =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Destino não encontrado. ID: "
                                                + id
                                )
                        );

        DestinationType destinationType =
                destinationTypeRepository
                        .findById(dto.getDestinationTypeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Tipo de destino não encontrado. ID: "
                                                + dto.getDestinationTypeId()
                                )
                        );

        destination.setName(
                dto.getName()
        );

        destination.setDescription(
                dto.getDescription()
        );

        destination.setDistanceKm(
                dto.getDistanceKm()
        );

        destination.setBasePrice(
                dto.getBasePrice()
        );

        destination.setCapacity(
                dto.getCapacity()
        );

        destination.setImageUrl(
                dto.getImageUrl()
        );

        destination.setDestinationType(
                destinationType
        );

        Destination updated =
                repository.save(destination);

        return mapper.toResponseDTO(updated);
    }

    public void delete(Long id) {

        Destination destination =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Destino não encontrado. ID: "
                                                + id
                                )
                        );

        repository.delete(destination);
    }

    @Transactional(readOnly = true)
    public List<DestinationResponseDTO> searchByName(
            String name) {

        return repository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}