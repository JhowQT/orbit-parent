package com.orbitbook.aiservice.ai;

import com.orbitbook.aiservice.dto.DestinationDTO;
import com.orbitbook.aiservice.feign.BookingClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SemanticSearchService {

    @Autowired(required = false)
    private EmbeddingModel embeddingModel;

    private final BookingClient bookingClient;

    private final List<DestinationEntry> index = new ArrayList<>();

    @PostConstruct
    public void buildIndex() {
        if (embeddingModel == null) {
            log.warn("[RAG] EmbeddingModel não disponível — busca semântica desabilitada.");
            return;
        }
        try {
            List<DestinationDTO> destinations =
                    bookingClient.getAllDestinations();

            for (DestinationDTO dto : destinations) {

                String text = buildText(dto);
                float[] embedding = embed(text);

                index.add(new DestinationEntry(dto, embedding));
            }

            log.info("[RAG] {} destinos indexados com embeddings.",
                    index.size());

        } catch (Exception ex) {
            log.warn("[RAG] Não foi possível indexar destinos: {}",
                    ex.getMessage());
        }
    }

    public List<DestinationDTO> findRelevant(String query, int topK) {

        if (index.isEmpty() || embeddingModel == null) {
            return bookingClient.getAllDestinations()
                    .stream()
                    .limit(topK)
                    .toList();
        }

        float[] queryEmbedding = embed(query);

        return index.stream()
                .sorted(Comparator.comparingDouble(
                        e -> -cosineSimilarity(queryEmbedding, e.embedding()))
                )
                .limit(topK)
                .map(DestinationEntry::destination)
                .toList();
    }

    private float[] embed(String text) {
        return embeddingModel.embed(text);
    }

    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot   += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0 || normB == 0) return 0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private String buildText(DestinationDTO dto) {
        return String.format(
                "Destino: %s. Descrição: %s. " +
                "Preço base: R$ %s. Capacidade: %d pessoas.",
                dto.getName(),
                dto.getDescription(),
                dto.getBasePrice(),
                dto.getCapacity()
        );
    }

    private record DestinationEntry(
            DestinationDTO destination,
            float[] embedding) {}
}
