package com.orbitbook.aiservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponseDTO {
    private String content;
    private List<String> suggestions;
    private Long recomendacaoId;
    private List<DestinoRecomendadoDTO> destinosRecomendados;
}
