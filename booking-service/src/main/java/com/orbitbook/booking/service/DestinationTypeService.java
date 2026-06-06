package com.orbitbook.booking.service;

import com.orbitbook.booking.dto.destinationtype.DestinationTypeCreateDTO;
import com.orbitbook.booking.dto.destinationtype.DestinationTypeResponseDTO;
import com.orbitbook.booking.dto.destinationtype.DestinationTypeUpdateDTO;
import com.orbitbook.booking.entity.DestinationType;
import com.orbitbook.booking.exception.ResourceNotFoundException;
import com.orbitbook.booking.mapper.DestinationTypeMapper;
import com.orbitbook.booking.repository.DestinationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DestinationTypeService {

    private final DestinationTypeRepository repository;
    private final DestinationTypeMapper mapper;

    public DestinationTypeResponseDTO create(
            DestinationTypeCreateDTO dto) {

        DestinationType destinationType =
                mapper.toEntity(dto);

        destinationType.setCreatedAt(
                LocalDateTime.now()
        );

        DestinationType saved =
                repository.save(destinationType);

        return mapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public DestinationTypeResponseDTO findById(
            Long id) {

        DestinationType destinationType =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Tipo de destino não encontrado. ID: "
                                                + id
                                )
                        );

        return mapper.toResponseDTO(
                destinationType
        );
    }

    @Transactional(readOnly = true)
    public List<DestinationTypeResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public DestinationTypeResponseDTO update(
            Long id,
            DestinationTypeUpdateDTO dto) {

        DestinationType destinationType =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Tipo de destino não encontrado. ID: "
                                                + id
                                )
                        );

        destinationType.setName(
                dto.getName()
        );

        destinationType.setDescription(
                dto.getDescription()
        );

        DestinationType updated =
                repository.save(destinationType);

        return mapper.toResponseDTO(updated);
    }

    public void delete(Long id) {

        DestinationType destinationType =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Tipo de destino não encontrado. ID: "
                                                + id
                                )
                        );

        repository.delete(destinationType);
    }
}