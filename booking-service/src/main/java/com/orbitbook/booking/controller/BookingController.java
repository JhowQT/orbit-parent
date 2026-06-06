package com.orbitbook.booking.controller;

import com.orbitbook.booking.dto.booking.BookingCreateDTO;
import com.orbitbook.booking.dto.booking.BookingResponseDTO;
import com.orbitbook.booking.dto.booking.BookingUpdateDTO;
import com.orbitbook.booking.hateoas.BookingModelAssembler;
import com.orbitbook.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

```
private final BookingService service;

private final BookingModelAssembler assembler;

@PostMapping
public ResponseEntity<EntityModel<BookingResponseDTO>> create(
        @RequestBody BookingCreateDTO dto) {

    BookingResponseDTO response =
            service.create(dto);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                    assembler.toModel(response)
            );
}

@GetMapping("/{id}")
public ResponseEntity<EntityModel<BookingResponseDTO>> findById(
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
                EntityModel<BookingResponseDTO>
                >
        > findAll() {

    List<EntityModel<BookingResponseDTO>> bookings =
            service.findAll()
                    .stream()
                    .map(assembler::toModel)
                    .toList();

    return ResponseEntity.ok(
            CollectionModel.of(bookings)
    );
}

@GetMapping("/user/{userId}")
public ResponseEntity<
        CollectionModel<
                EntityModel<BookingResponseDTO>
                >
        > findByUser(
        @PathVariable Long userId) {

    List<EntityModel<BookingResponseDTO>> bookings =
            service.findByUser(userId)
                    .stream()
                    .map(assembler::toModel)
                    .toList();

    return ResponseEntity.ok(
            CollectionModel.of(bookings)
    );
}

@PutMapping("/{id}")
public ResponseEntity<EntityModel<BookingResponseDTO>> update(
        @PathVariable Long id,
        @RequestBody BookingUpdateDTO dto) {

    return ResponseEntity.ok(
            assembler.toModel(
                    service.update(id, dto)
            )
    );
}

@PatchMapping("/{id}/cancel")
public ResponseEntity<EntityModel<BookingResponseDTO>> cancelBooking(
        @PathVariable Long id) {

    return ResponseEntity.ok(
            assembler.toModel(
                    service.cancelBooking(id)
            )
    );
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(
        @PathVariable Long id) {

    service.delete(id);

    return ResponseEntity.noContent().build();
}
```

}
