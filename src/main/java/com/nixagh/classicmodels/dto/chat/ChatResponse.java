package com.nixagh.classicmodels.dto.chat;

import com.nixagh.classicmodels.entity.chat.ChatMessage;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private boolean success;
    private String errorMessage;
    private List<ChatMessage> messages;
}
