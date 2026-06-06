package com.orbitbook.booking.controller;

import com.orbitbook.booking.dto.destination.DestinationCreateDTO;
import com.orbitbook.booking.dto.destination.DestinationResponseDTO;
import com.orbitbook.booking.dto.destination.DestinationUpdateDTO;
import com.orbitbook.booking.service.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService service;

    @PostMapping
    public ResponseEntity<DestinationResponseDTO> create(
            @RequestBody DestinationCreateDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<DestinationResponseDTO>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<DestinationResponseDTO>> searchByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                service.searchByName(name)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinationResponseDTO> update(
            @PathVariable Long id,
            @RequestBody DestinationUpdateDTO dto) {

        return ResponseEntity.ok(
                service.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}