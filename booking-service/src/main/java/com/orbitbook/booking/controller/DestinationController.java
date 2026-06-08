package com.orbitbook.booking.controller;

import com.orbitbook.booking.dto.destination.DestinationCreateDTO;
import com.orbitbook.booking.dto.destination.DestinationPageDTO;
import com.orbitbook.booking.dto.destination.DestinationResponseDTO;
import com.orbitbook.booking.dto.destination.DestinationUpdateDTO;
import com.orbitbook.booking.hateoas.DestinationModelAssembler;
import com.orbitbook.booking.service.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class DestinationController {

private final DestinationService service;

private final DestinationModelAssembler assembler;

@PostMapping
public ResponseEntity<EntityModel<DestinationResponseDTO>> create(
        @RequestBody DestinationCreateDTO dto) {

    DestinationResponseDTO response =
            service.create(dto);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                    assembler.toModel(response)
            );
}

@GetMapping("/{id}")
public ResponseEntity<EntityModel<DestinationResponseDTO>> findById(
        @PathVariable Long id) {

    return ResponseEntity.ok(
            assembler.toModel(
                    service.findById(id)
            )
    );
}

@GetMapping
public ResponseEntity<
        CollectionModel<
                EntityModel<DestinationResponseDTO>
                >
        > findAll() {

    List<EntityModel<DestinationResponseDTO>> destinations =
            service.findAll()
                    .stream()
                    .map(assembler::toModel)
                    .toList();

    return ResponseEntity.ok(
            CollectionModel.of(destinations)
    );
}

@GetMapping(value = "/all", produces = "application/json")
public ResponseEntity<List<DestinationResponseDTO>> findAllPlain() {
    return ResponseEntity.ok(service.findAll());
}

@GetMapping("/search")
public ResponseEntity<
        CollectionModel<
                EntityModel<DestinationResponseDTO>
                >
        > searchByName(
        @RequestParam String name) {

    List<EntityModel<DestinationResponseDTO>> destinations =
            service.searchByName(name)
                    .stream()
                    .map(assembler::toModel)
                    .toList();

    return ResponseEntity.ok(
            CollectionModel.of(destinations)
    );
}

@PutMapping("/{id}")
public ResponseEntity<EntityModel<DestinationResponseDTO>> update(
        @PathVariable Long id,
        @RequestBody DestinationUpdateDTO dto) {

    return ResponseEntity.ok(
            assembler.toModel(
                    service.update(id, dto)
            )
    );
}

@GetMapping("/page")
public ResponseEntity<DestinationPageDTO> findFiltered(
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) BigDecimal precoMin,
        @RequestParam(required = false) BigDecimal precoMax,
        @RequestParam(required = false) String busca,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int limit) {

    return ResponseEntity.ok(
            service.findFiltered(tipo, precoMin, precoMax, busca, page, limit)
    );
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(
        @PathVariable Long id) {

    service.delete(id);

    return ResponseEntity.noContent().build();
}

}
