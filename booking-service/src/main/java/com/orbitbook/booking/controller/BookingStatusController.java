package com.orbitbook.booking.controller;

import com.orbitbook.booking.dto.bookingstatus.BookingStatusCreateDTO;
import com.orbitbook.booking.dto.bookingstatus.BookingStatusResponseDTO;
import com.orbitbook.booking.dto.bookingstatus.BookingStatusUpdateDTO;
import com.orbitbook.booking.service.BookingStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking-statuses")
@RequiredArgsConstructor
public class BookingStatusController {

    private final BookingStatusService service;

    @PostMapping
    public ResponseEntity<BookingStatusResponseDTO> create(
            @RequestBody BookingStatusCreateDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingStatusResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<BookingStatusResponseDTO>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingStatusResponseDTO> update(
            @PathVariable Long id,
            @RequestBody BookingStatusUpdateDTO dto) {

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