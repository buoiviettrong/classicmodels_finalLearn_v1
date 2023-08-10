package com.nixagh.classicmodels.service.chat_service;

import com.nixagh.classicmodels.dto.chat.*;
import jakarta.servlet.http.HttpServletRequest;

public interface IChatService {
    RoomResponse createRoom(String roomName, long creator);

    RoomResponse joinRoom(String roomId, long username);

    RoomResponse leaveRoom(String roomId, String username);

    ChatResponse sendMessage(ChatRequest message, String roomId);

    ChatResponse getMessages(String roomId);

    GetRoomResponse getRoom(String roomId);

    RoomResponse getRooms(long memberId);

    RoomResponse deleteRoom(String roomId, long memberId);

    RoomResponse addMember(String roomId, long memberId);

    GetMemberResponse getMembers(String roomId);

    RoomResponse deleteMember(String roomId, long memberId, HttpServletRequest request);

    RoomResponse banUnbanMember(String roomId, long memberId, HttpServletRequest request, boolean isBan);
}
