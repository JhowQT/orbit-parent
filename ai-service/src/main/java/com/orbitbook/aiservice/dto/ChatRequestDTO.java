package com.orbitbook.aiservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDTO {
    private List<MensagemChatDTO> messages;
    private Long userId;
}
