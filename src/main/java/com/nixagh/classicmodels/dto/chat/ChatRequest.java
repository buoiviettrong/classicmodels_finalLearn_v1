package com.nixagh.classicmodels.dto.chat;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    private long senderId;
    private long receiverId;
    private String senderName;
    private String receiverName;
    private String content;
}
