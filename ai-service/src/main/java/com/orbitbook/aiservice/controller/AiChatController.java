package com.orbitbook.aiservice.controller;

import com.orbitbook.aiservice.dto.ChatRequestDTO;
import com.orbitbook.aiservice.dto.ChatResponseDTO;
import com.orbitbook.aiservice.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
@Tag(name = "AI Chat", description = "Chat multi-turno com ARIA — assistente de turismo espacial")
public class AiChatController {

    private final AiChatService aiChatService;

    @Operation(
            summary = "Conversar com ARIA",
            description = "Envie o histórico completo de mensagens para continuar a conversa. " +
                          "A IA pode recomendar destinos via tag [REC:ID] e retorna sugestões contextuais."
    )
    @ApiResponse(responseCode = "200", description = "Resposta gerada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida (mensagens vazias)")
    @PostMapping
    public ResponseEntity<ChatResponseDTO> chat(
            @RequestBody ChatRequestDTO request) {

        return ResponseEntity.ok(aiChatService.chat(request));
    }
}
