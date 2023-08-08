package com.nixagh.classicmodels.service.chat_service;

import com.nixagh.classicmodels.dto.chat.*;

public interface IChatService {
    RoomResponse createRoom(String roomName, long creator);
    RoomResponse joinRoom(String roomId, long username);
    RoomResponse leaveRoom(String roomId, String username);
    ChatResponse sendMessage(ChatRequest message, String roomId);
    ChatResponse getMessages(String roomId);
    GetRoomResponse getRoom(String roomId);

    RoomResponse getRooms(long memberId);
}
