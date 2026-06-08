package com.orbitbook.booking.controller;

import com.orbitbook.booking.dto.review.ReviewCreateDTO;
import com.orbitbook.booking.dto.review.ReviewResponseDTO;
import com.orbitbook.booking.dto.review.ReviewUpdateDTO;
import com.orbitbook.booking.dto.review.ReviewWithUserDTO;
import com.orbitbook.booking.hateoas.ReviewModelAssembler;
import com.orbitbook.booking.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

private final ReviewService service;

private final ReviewModelAssembler assembler;

@PostMapping
public ResponseEntity<EntityModel<ReviewResponseDTO>> create(
        @RequestBody ReviewCreateDTO dto) {

    ReviewResponseDTO response =
            service.createReview(dto);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                    assembler.toModel(response)
            );
}

@GetMapping("/{id}")
public ResponseEntity<EntityModel<ReviewResponseDTO>> findById(
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
                EntityModel<ReviewResponseDTO>
                >
        > findAll() {

    List<EntityModel<ReviewResponseDTO>> reviews =
            service.findAll()
                    .stream()
                    .map(assembler::toModel)
                    .toList();

    return ResponseEntity.ok(
            CollectionModel.of(reviews)
    );
}

@GetMapping("/booking/{bookingId}")
public ResponseEntity<
        CollectionModel<
                EntityModel<ReviewResponseDTO>
                >
        > findByBooking(
        @PathVariable Long bookingId) {

    List<EntityModel<ReviewResponseDTO>> reviews =
            service.findByBooking(bookingId)
                    .stream()
                    .map(assembler::toModel)
                    .toList();

    return ResponseEntity.ok(
            CollectionModel.of(reviews)
    );
}

@PutMapping("/{id}")
public ResponseEntity<EntityModel<ReviewResponseDTO>> update(
        @PathVariable Long id,
        @RequestBody ReviewUpdateDTO dto) {

    return ResponseEntity.ok(
            assembler.toModel(
                    service.update(id, dto)
            )
    );
}

@GetMapping("/destination/{destinationId}")
public ResponseEntity<List<ReviewWithUserDTO>> findByDestination(
        @PathVariable Long destinationId) {

    return ResponseEntity.ok(
            service.findByDestination(destinationId)
    );
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(
        @PathVariable Long id) {

    service.delete(id);

    return ResponseEntity.noContent().build();
}

}
