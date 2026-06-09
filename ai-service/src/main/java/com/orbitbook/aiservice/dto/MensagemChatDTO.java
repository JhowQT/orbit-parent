package com.orbitbook.aiservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensagemChatDTO {
    private String role;
    private String content;
}
