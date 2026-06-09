package com.orbitbook.aiservice.service;

import com.orbitbook.aiservice.ai.DestinationTools;
import com.orbitbook.aiservice.ai.SemanticSearchService;
import com.orbitbook.aiservice.dto.DestinationDTO;
import com.orbitbook.aiservice.dto.RecommendationRequestDTO;
import com.orbitbook.aiservice.dto.RecommendationResponseDTO;
import com.orbitbook.aiservice.entity.AiRecommendation;
import com.orbitbook.aiservice.feign.AuthClient;
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
    private final AuthClient authClient;
    private final ChatClient chatClient;
    private final SemanticSearchService semanticSearchService;
    private final DestinationTools destinationTools;

    public RecommendationResponseDTO generateRecommendation(
            RecommendationRequestDTO request) {

        authClient.findUserById(request.getUserId());

        // RAG: busca semântica — recupera os 3 destinos mais
        // relevantes para o prompt, em vez de enviar todos ao modelo
        List<DestinationDTO> relevantDestinations =
                semanticSearchService.findRelevant(
                        request.getPrompt(), 3
                );

        String ragContext = buildRagContext(relevantDestinations);

        String systemPrompt = """
                Você é um especialista em turismo espacial da plataforma OrbitBook.
                Use os destinos fornecidos como contexto principal.
                Se precisar de mais informações, utilize as ferramentas disponíveis.
                Seja objetivo e personalizado na sua recomendação.
                """;

        String userPrompt = String.format(
                """
                Contexto — destinos mais relevantes para esta solicitação:
                %s

                Solicitação do usuário: %s
                """,
                ragContext,
                request.getPrompt()
        );

        // Tooling: o Gemini pode chamar DestinationTools autonomamente
        // durante a geração se precisar de dados adicionais (ex: filtrar por
        // orçamento, buscar histórico do usuário)
        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .tools(destinationTools)
                .call()
                .content();

        if (response == null || response.isBlank()) {
            throw new RuntimeException(
                    "A IA não retornou nenhuma recomendação."
            );
        }

        AiRecommendation recommendation = AiRecommendation.builder()
                .promptUsed(request.getPrompt())
                .responseText(response)
                .modelUsed("gemini-2.5-flash")
                .createdAt(LocalDateTime.now())
                .userId(request.getUserId())
                .build();

        recommendation = repository.save(recommendation);

        return mapper.toResponseDTO(recommendation);
    }

    private String buildRagContext(List<DestinationDTO> destinations) {
        StringBuilder sb = new StringBuilder();
        for (DestinationDTO d : destinations) {
            sb.append("- ").append(d.getName())
              .append(": ").append(d.getDescription())
              .append(" | Preço: R$ ").append(d.getBasePrice())
              .append(" | Capacidade: ").append(d.getCapacity())
              .append(" pessoas\n");
        }
        return sb.toString();
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