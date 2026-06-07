package com.orbitbook.aiservice.service;

import com.orbitbook.aiservice.dto.DestinationDTO;
import com.orbitbook.aiservice.dto.RecommendationRequestDTO;
import com.orbitbook.aiservice.dto.RecommendationResponseDTO;
import com.orbitbook.aiservice.entity.AiRecommendation;
import com.orbitbook.aiservice.feign.AuthClient;
import com.orbitbook.aiservice.feign.BookingClient;
import com.orbitbook.aiservice.feign.dto.UserResponseDTO;
import com.orbitbook.aiservice.mapper.AiRecommendationMapper;
import com.orbitbook.aiservice.repository.AiRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AiRecommendationService {

    private final AiRecommendationRepository repository;

    private final AiRecommendationMapper mapper;

    private final BookingClient bookingClient;

    private final AuthClient authClient;

    private final ChatClient chatClient;

    public RecommendationResponseDTO generateRecommendation(
            RecommendationRequestDTO request) {

        UserResponseDTO user =
                authClient.findUserById(
                        request.getUserId()
                );

        if (user == null) {
            throw new RuntimeException(
                    "Usuário não encontrado."
            );
        }

        List<DestinationDTO> destinations =
                bookingClient.getAllDestinations();

        StringBuilder promptBuilder =
                new StringBuilder();

        promptBuilder.append("""
                Você é um especialista em turismo da plataforma OrbitBook.

                Analise os destinos disponíveis abaixo e recomende os mais adequados para o usuário.

                Destinos disponíveis:

                """);

        for (DestinationDTO destination : destinations) {

            promptBuilder.append("Destino: ")
                    .append(destination.getName())
                    .append("\n");

            promptBuilder.append("Descrição: ")
                    .append(destination.getDescription())
                    .append("\n");

            promptBuilder.append("Preço Base: ")
                    .append(destination.getBasePrice())
                    .append("\n");

            promptBuilder.append("Capacidade: ")
                    .append(destination.getCapacity())
                    .append("\n\n");
        }

        promptBuilder.append("""
                Solicitação do usuário:

                """);

        promptBuilder.append(
                request.getPrompt()
        );

        String response =
                chatClient.prompt()
                        .user(promptBuilder.toString())
                        .call()
                        .content();

        AiRecommendation recommendation =
                AiRecommendation.builder()
                        .promptUsed(
                                request.getPrompt()
                        )
                        .responseText(
                                response
                        )
                        .modelUsed(
                                "gemini-2.5-flash"
                        )
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .userId(
                                request.getUserId()
                        )
                        .build();

        recommendation =
                repository.save(
                        recommendation
                );

        return mapper.toResponseDTO(
                recommendation
        );
    }

    @Transactional(readOnly = true)
    public RecommendationResponseDTO findById(
            Long id) {

        AiRecommendation recommendation =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recomendação não encontrada. ID: "
                                                + id
                                )
                        );

        return mapper.toResponseDTO(
                recommendation
        );
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponseDTO> findByUser(
            Long userId) {

        return repository.findByUserId(userId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}