package com.orbitbook.aiservice.mapper;

import com.orbitbook.aiservice.dto.RecommendationResponseDTO;
import com.orbitbook.aiservice.entity.AiRecommendation;
import org.springframework.stereotype.Component;

@Component
public class AiRecommendationMapper {

    public RecommendationResponseDTO toResponseDTO(AiRecommendation entity) {

        if (entity == null) {
            return null;
        }

        return RecommendationResponseDTO.builder()
                .id(entity.getId())
                .promptUsed(entity.getPromptUsed())
                .responseText(entity.getResponseText())
                .modelUsed(entity.getModelUsed())
                .createdAt(entity.getCreatedAt())
                .userId(entity.getUserId())
                .build();
    }
}