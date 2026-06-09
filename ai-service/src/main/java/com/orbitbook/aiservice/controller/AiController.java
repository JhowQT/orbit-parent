package com.orbitbook.aiservice.controller;

import com.orbitbook.aiservice.dto.RecommendationRequestDTO;
import com.orbitbook.aiservice.dto.RecommendationResponseDTO;
import com.orbitbook.aiservice.service.AiRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/recommendations")
@RequiredArgsConstructor
@Tag(name = "AI Recommendations", description = "Recomendações de destinos geradas com IA (RAG + Tooling + MCP via Gemini)")
public class AiController {

    private final AiRecommendationService aiRecommendationService;

    @Operation(
            summary = "Gerar recomendação personalizada",
            description = "Usa RAG para recuperar destinos relevantes semanticamente e Tooling para o Gemini buscar dados adicionais autonomamente."
    )
    @ApiResponse(responseCode = "201", description = "Recomendação gerada e persistida com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    @PostMapping
    public ResponseEntity<RecommendationResponseDTO> generateRecommendation(
            @Valid @RequestBody RecommendationRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(aiRecommendationService.generateRecommendation(request));
    }

    @Operation(summary = "Buscar recomendação por ID")
    @ApiResponse(responseCode = "200", description = "Recomendação encontrada")
    @ApiResponse(responseCode = "404", description = "Recomendação não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<RecommendationResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(aiRecommendationService.findById(id));
    }

    @Operation(summary = "Listar recomendações de um usuário")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationResponseDTO>> findByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(aiRecommendationService.findByUser(userId));
    }

    @Operation(summary = "Listar todas as recomendações")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<RecommendationResponseDTO>> findAll() {

        return ResponseEntity.ok(aiRecommendationService.findAll());
    }
}
