package com.orbitbook.aiservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AI_RECOMENDATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ai_recomendation")
    @SequenceGenerator(name = "seq_ai_recomendation", sequenceName = "SEQ_AI_RECOMENDATION", allocationSize = 1)
    @Column(name = "ID_AI_RECOMENDATION")
    private Long id;

    @Column(name = "PROMPT_USED", nullable = false, length = 700)
    private String promptUsed;

    @Column(name = "RESPONSE_TEXT", nullable = false, length = 500)
    private String responseText;

    @Column(name = "MODEL_USED", nullable = false, length = 50)
    private String modelUsed;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    
    @Column(name = "ID_USERS_ORBIT", nullable = false)
    private Long userId;
}