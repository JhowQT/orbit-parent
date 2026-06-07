package com.orbitbook.aiservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationRequestDTO {

    @NotNull
    private Long userId;

    @NotBlank
    private String prompt;
}