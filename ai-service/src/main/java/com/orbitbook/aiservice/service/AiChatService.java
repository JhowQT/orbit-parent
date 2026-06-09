package com.orbitbook.aiservice.service;

import com.orbitbook.aiservice.dto.*;
import com.orbitbook.aiservice.entity.AiRecommendation;
import com.orbitbook.aiservice.feign.BookingClient;
import com.orbitbook.aiservice.repository.AiRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class AiChatService {

    private static final Pattern REC_PATTERN =
            Pattern.compile("\\[REC:([\\d,\\s]+)\\]");

    private static final String SYSTEM_PROMPT = """
            Você é ARIA, assistente de viagens espaciais do OrbitBook.

            ESTILO: Respostas curtas e diretas — 1 a 3 frases. Tom animado, em português do Brasil.

            REGRA OBRIGATÓRIA — TAG DE RECOMENDAÇÃO:
            Sempre que recomendar um destino específico, inclua ao final: [REC:ID1,ID2]
            usando os IDs exatos dos destinos (máximo 3). Essa tag é removida antes de exibir ao usuário.
            Se não recomendar destino específico, não inclua a tag.
            """;

    private final ChatClient chatClient;
    private final BookingClient bookingClient;
    private final AiRecommendationRepository recommendationRepository;

    public ChatResponseDTO chat(ChatRequestDTO request) {

        if (request.getMessages() == null || request.getMessages().isEmpty()) {
            throw new IllegalArgumentException("Mensagens não podem estar vazias.");
        }

        String userContext = buildUserContext(request.getMessages());

        String rawResponse = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userContext)
                .call()
                .content();

        if (rawResponse == null || rawResponse.isBlank()) {
            rawResponse = "Desculpe, não consegui processar sua mensagem. Tente novamente.";
        }

        List<Long> destIds = extractRecommendationIds(rawResponse);
        String cleanContent = rawResponse.replaceAll("\\s*\\[REC:[\\d,\\s]+\\]", "").trim();

        List<DestinoRecomendadoDTO> destinos = fetchDestinos(destIds);

        String lastUserMsg = request.getMessages().stream()
                .filter(m -> "user".equalsIgnoreCase(m.getRole()))
                .reduce((first, second) -> second)
                .map(MensagemChatDTO::getContent)
                .orElse("");

        List<String> suggestions = generateSuggestions(lastUserMsg, cleanContent);

        Long recomendacaoId = null;
        if (request.getUserId() != null) {
            try {
                AiRecommendation rec = AiRecommendation.builder()
                        .promptUsed(truncate(lastUserMsg, 700))
                        .responseText(truncate(cleanContent, 490))
                        .modelUsed("gemini-2.5-flash")
                        .createdAt(LocalDateTime.now())
                        .userId(request.getUserId())
                        .build();
                recomendacaoId = recommendationRepository.save(rec).getId();
            } catch (Exception ignored) {
            }
        }

        return ChatResponseDTO.builder()
                .content(cleanContent)
                .suggestions(suggestions)
                .recomendacaoId(recomendacaoId)
                .destinosRecomendados(destinos)
                .build();
    }

    private String buildUserContext(List<MensagemChatDTO> history) {
        if (history.size() == 1) {
            return history.get(0).getContent();
        }
        StringBuilder ctx = new StringBuilder("Histórico da conversa:\n");
        for (int i = 0; i < history.size() - 1; i++) {
            MensagemChatDTO m = history.get(i);
            String label = "user".equalsIgnoreCase(m.getRole()) ? "Usuário" : "ARIA";
            ctx.append(label).append(": ").append(m.getContent()).append("\n");
        }
        ctx.append("\nNova mensagem do usuário: ")
           .append(history.get(history.size() - 1).getContent());
        return ctx.toString();
    }

    private List<Long> extractRecommendationIds(String response) {
        List<Long> ids = new ArrayList<>();
        Matcher matcher = REC_PATTERN.matcher(response);
        if (matcher.find()) {
            for (String part : matcher.group(1).split(",")) {
                try {
                    ids.add(Long.parseLong(part.trim()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return ids;
    }

    private List<DestinoRecomendadoDTO> fetchDestinos(List<Long> ids) {
        List<DestinoRecomendadoDTO> result = new ArrayList<>();
        for (Long id : ids) {
            try {
                var dest = bookingClient.getDestinationById(id);
                result.add(DestinoRecomendadoDTO.builder()
                        .id(dest.getIdDestinations())
                        .name(dest.getName())
                        .description(dest.getDescription())
                        .basePrice(dest.getBasePrice())
                        .capacity(dest.getCapacity())
                        .imageUrl(dest.getImageUrl())
                        .destinationTypeName(dest.getDestinationTypeName())
                        .build());
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    private List<String> generateSuggestions(String userMsg, String response) {
        String text = (userMsg + " " + response).toLowerCase();
        if (text.contains("lua") || text.contains("lunar")) {
            return List.of("Quais são os requisitos físicos?", "Como é o treinamento?", "Ver outras opções");
        }
        if (text.contains("marte") || text.contains("marciano")) {
            return List.of("Quem pode se candidatar?", "Como funciona a seleção?", "Ver outros destinos");
        }
        if (text.contains("preço") || text.contains("valor") || text.contains("custo") || text.contains("quanto")) {
            return List.of("Como funciona o pagamento?", "Destino mais barato disponível?", "Simular valor total");
        }
        if (text.contains("suborbital") || text.contains("orbital")) {
            return List.of("Quanto tempo dura a missão?", "Como funciona o treinamento?", "Quero reservar");
        }
        return List.of("Destinos mais acessíveis", "Missões com alta avaliação", "Opções para iniciantes");
    }

    private String truncate(String text, int maxChars) {
        if (text == null) return "";
        return text.length() <= maxChars ? text : text.substring(0, maxChars);
    }
}
