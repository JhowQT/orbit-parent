package com.orbitbook.booking.controller;

import com.orbitbook.booking.dto.payment.PaymentCreateDTO;
import com.orbitbook.booking.dto.payment.PaymentResponseDTO;
import com.orbitbook.booking.dto.payment.PaymentUpdateDTO;
import com.orbitbook.booking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> create(
            @RequestBody PaymentCreateDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createPayment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> findAll() {

        return ResponseEntity.ok(
                service.findAll()
        );
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentResponseDTO>> findByBooking(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                service.findByBooking(bookingId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> update(
            @PathVariable Long id,
            @RequestBody PaymentUpdateDTO dto) {

        return ResponseEntity.ok(
                service.update(id, dto)
        );
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<PaymentResponseDTO> approvePayment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.approvePayment(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}