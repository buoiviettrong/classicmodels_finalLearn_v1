package com.nixagh.classicmodels.dto.Message;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String messageId;
    private String message;
}
