package com.nixagh.classicmodels.dto.chat;

import com.nixagh.classicmodels.entity.chat.ChatMessage;

import java.util.List;

public class GetRoomResponse {
    private String roomId;
    private String roomName;
    private Long owner;
//    private List<Long> members;
    private List<ChatMessage> messages;
}
