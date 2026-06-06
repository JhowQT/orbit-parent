package com.orbitbook.booking.controller;

import com.orbitbook.booking.dto.booking.BookingCreateDTO;
import com.orbitbook.booking.dto.booking.BookingResponseDTO;
import com.orbitbook.booking.dto.booking.BookingUpdateDTO;
import com.orbitbook.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(
            @RequestBody BookingCreateDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> findByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                service.findByUser(userId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> update(
            @PathVariable Long id,
            @RequestBody BookingUpdateDTO dto) {

        return ResponseEntity.ok(
                service.update(id, dto)
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.cancelBooking(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}