package com.orbitbook.booking.controller;

import com.orbitbook.booking.dto.destinationtype.DestinationTypeCreateDTO;
import com.orbitbook.booking.dto.destinationtype.DestinationTypeResponseDTO;
import com.orbitbook.booking.dto.destinationtype.DestinationTypeUpdateDTO;
import com.orbitbook.booking.service.DestinationTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destination-types")
@RequiredArgsConstructor
public class DestinationTypeController {

    private final DestinationTypeService service;

    @PostMapping
    public ResponseEntity<DestinationTypeResponseDTO> create(
            @RequestBody DestinationTypeCreateDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationTypeResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<DestinationTypeResponseDTO>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinationTypeResponseDTO> update(
            @PathVariable Long id,
            @RequestBody DestinationTypeUpdateDTO dto) {

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