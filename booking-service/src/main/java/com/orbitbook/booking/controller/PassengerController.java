package com.orbitbook.booking.controller;

import com.orbitbook.booking.dto.passenger.PassengerCreateDTO;
import com.orbitbook.booking.dto.passenger.PassengerResponseDTO;
import com.orbitbook.booking.dto.passenger.PassengerUpdateDTO;
import com.orbitbook.booking.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService service;

    @PostMapping
    public ResponseEntity<PassengerResponseDTO> create(
            @RequestBody PassengerCreateDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<PassengerResponseDTO>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponseDTO> update(
            @PathVariable Long id,
            @RequestBody PassengerUpdateDTO dto) {

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