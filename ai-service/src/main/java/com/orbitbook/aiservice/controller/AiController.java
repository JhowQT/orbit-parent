package com.orbitbook.aiservice.controller;

import com.orbitbook.aiservice.dto.RecommendationRequestDTO;
import com.orbitbook.aiservice.dto.RecommendationResponseDTO;
import com.orbitbook.aiservice.service.AiRecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/recommendations")
@RequiredArgsConstructor
public class AiController {

    private final AiRecommendationService aiRecommendationService;

    @PostMapping
    public ResponseEntity<RecommendationResponseDTO> generateRecommendation(
            @Valid @RequestBody RecommendationRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        aiRecommendationService.generateRecommendation(
                                request
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendationResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                aiRecommendationService.findById(
                        id
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationResponseDTO>> findByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                aiRecommendationService.findByUser(
                        userId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<RecommendationResponseDTO>> findAll() {

        return ResponseEntity.ok(
                aiRecommendationService.findAll()
        );
    }
}