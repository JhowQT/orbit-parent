package com.orbitbook.booking.service;

import com.orbitbook.booking.dto.destination.AvaliacaoResumoDTO;
import com.orbitbook.booking.dto.destination.DestinationCreateDTO;
import com.orbitbook.booking.dto.destination.DestinationPageDTO;
import com.orbitbook.booking.dto.destination.DestinationResponseDTO;
import com.orbitbook.booking.dto.destination.DestinationUpdateDTO;
import com.orbitbook.booking.entity.Destination;
import com.orbitbook.booking.entity.DestinationType;
import com.orbitbook.booking.entity.Review;
import com.orbitbook.booking.exception.ResourceNotFoundException;
import com.orbitbook.booking.mapper.DestinationMapper;
import com.orbitbook.booking.repository.DestinationRepository;
import com.orbitbook.booking.repository.DestinationTypeRepository;
import com.orbitbook.booking.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DestinationService {

    private final DestinationRepository repository;

    private final DestinationTypeRepository destinationTypeRepository;

    private final DestinationMapper mapper;

    private final ReviewRepository reviewRepository;

    @CacheEvict(
            value = {
                    "destinations",
                    "destinations-all"
            },
            allEntries = true
    )
    public DestinationResponseDTO create(
            DestinationCreateDTO dto) {

        validateDestination(dto);

        DestinationType destinationType =
                destinationTypeRepository
                        .findById(
                                dto.getDestinationTypeId()
                        )
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

        return enrichWithAvaliacao(saved);
    }

    @Cacheable(
            value = "destinations",
            key = "#id"
    )
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

        return enrichWithAvaliacao(destination);
    }

    @Cacheable("destinations-all")
    @Transactional(readOnly = true)
    public List<DestinationResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(this::enrichWithAvaliacao)
                .toList();
    }

    @Transactional(readOnly = true)
    public DestinationPageDTO findFiltered(
            String tipo,
            BigDecimal precoMin,
            BigDecimal precoMax,
            String busca,
            int page,
            int limit) {

        Page<Destination> result = repository.findFiltered(
                tipo,
                precoMin,
                precoMax,
                busca,
                PageRequest.of(page - 1, limit)
        );

        List<DestinationResponseDTO> items = result.getContent()
                .stream()
                .map(this::enrichWithAvaliacao)
                .toList();

        int pages = (int) Math.ceil(
                (double) result.getTotalElements() / limit
        );

        return DestinationPageDTO.builder()
                .items(items)
                .total(result.getTotalElements())
                .page(page)
                .pages(pages)
                .limit(limit)
                .build();
    }

    @CacheEvict(
            value = {
                    "destinations",
                    "destinations-all"
            },
            allEntries = true
    )
    public DestinationResponseDTO update(
            Long id,
            DestinationUpdateDTO dto) {

        validateDestination(dto);

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
                        .findById(
                                dto.getDestinationTypeId()
                        )
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

        return enrichWithAvaliacao(updated);
    }

    @CacheEvict(
            value = {
                    "destinations",
                    "destinations-all"
            },
            allEntries = true
    )
    public void delete(
            Long id) {

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
                .findByNameContainingIgnoreCase(
                        name
                )
                .stream()
                .map(this::enrichWithAvaliacao)
                .toList();
    }

    private DestinationResponseDTO enrichWithAvaliacao(
            Destination destination) {

        DestinationResponseDTO dto =
                mapper.toResponseDTO(destination);

        List<Review> reviews =
                reviewRepository.findByDestinationId(
                        destination.getIdDestinations()
                );

        double media = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        dto.setAvaliacao(
                AvaliacaoResumoDTO.builder()
                        .media(reviews.isEmpty() ? 0.0 : media)
                        .total(reviews.size())
                        .build()
        );

        return dto;
    }

    private void validateDestination(
            DestinationCreateDTO dto) {

        if (dto.getName() == null
                || dto.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Nome do destino é obrigatório."
            );
        }

        if (dto.getDescription() == null
                || dto.getDescription().isBlank()) {

            throw new IllegalArgumentException(
                    "Descrição do destino é obrigatória."
            );
        }

        if (dto.getImageUrl() == null
                || dto.getImageUrl().isBlank()) {

            throw new IllegalArgumentException(
                    "Imagem do destino é obrigatória."
            );
        }

        if (dto.getDistanceKm() == null
                || dto.getDistanceKm() <= 0) {

            throw new IllegalArgumentException(
                    "A distância deve ser maior que zero."
            );
        }

        if (dto.getCapacity() == null
                || dto.getCapacity() <= 0) {

            throw new IllegalArgumentException(
                    "A capacidade deve ser maior que zero."
            );
        }

        if (dto.getBasePrice() == null
                || dto.getBasePrice().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "O preço base deve ser maior que zero."
            );
        }
    }

    private void validateDestination(
            DestinationUpdateDTO dto) {

        if (dto.getName() == null
                || dto.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Nome do destino é obrigatório."
            );
        }

        if (dto.getDescription() == null
                || dto.getDescription().isBlank()) {

            throw new IllegalArgumentException(
                    "Descrição do destino é obrigatória."
            );
        }

        if (dto.getImageUrl() == null
                || dto.getImageUrl().isBlank()) {

            throw new IllegalArgumentException(
                    "Imagem do destino é obrigatória."
            );
        }

        if (dto.getDistanceKm() == null
                || dto.getDistanceKm() <= 0) {

            throw new IllegalArgumentException(
                    "A distância deve ser maior que zero."
            );
        }

        if (dto.getCapacity() == null
                || dto.getCapacity() <= 0) {

            throw new IllegalArgumentException(
                    "A capacidade deve ser maior que zero."
            );
        }

        if (dto.getBasePrice() == null
                || dto.getBasePrice().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "O preço base deve ser maior que zero."
            );
        }
    }
}
