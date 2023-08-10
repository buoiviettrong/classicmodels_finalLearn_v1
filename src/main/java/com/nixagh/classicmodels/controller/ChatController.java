package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.dto.chat.ChatRequest;
import com.nixagh.classicmodels.dto.chat.ChatResponse;
import com.nixagh.classicmodels.dto.chat.GetMemberResponse;
import com.nixagh.classicmodels.dto.chat.RoomResponse;
import com.nixagh.classicmodels.service.chat_service.IChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final IChatService chatService;

    @MessageMapping("/chat")
    public ChatResponse sendMessage(
            @Payload ChatRequest message,
            @DestinationVariable String roomId
    ) {
        System.out.println("send-message" + message);
        System.out.println("send-message" + roomId);
        return chatService.sendMessage(message, roomId);
    }

    @PostMapping("/create-room")
    public RoomResponse createRoom(
            @RequestBody createRoomRequest request
    ) {
        System.out.println("create-room");
        return chatService.createRoom(request.roomName, request.creator);
    }

    public record createRoomRequest(String roomName, long creator) {
    }


    @GetMapping("/join-room")
    public RoomResponse joinRoom(
            @RequestParam(name = "roomId") String roomId,
            @RequestParam(name = "memberId") long memberId) {
        return chatService.joinRoom(roomId, memberId);
    }

    @GetMapping("/get-messages")
    public ChatResponse getMessages(
            @RequestParam(name = "roomId") String roomId) {
        return chatService.getMessages(roomId);
    }

    @PostMapping("/send-message")
    public ChatResponse sendMessage(
            @RequestParam(name = "roomId") String roomId,
            @RequestBody ChatRequest request) {
        return chatService.sendMessage(request, roomId);
    }

    @GetMapping("/get-rooms")
    public RoomResponse getRooms(@RequestParam(name = "memberId") long memberId) {
        return chatService.getRooms(memberId);
    }

    @DeleteMapping("/delete-room")
    public RoomResponse deleteRoom(@RequestParam(name = "roomId") String roomId, @RequestParam(name = "memberId") long memberId) {
        return chatService.deleteRoom(roomId, memberId);
    }

    @PostMapping("/add-member")
    public RoomResponse addMember(
            @RequestParam(name = "roomId") String roomId,
            @RequestParam(name = "memberId") long memberId) {
        return chatService.addMember(roomId, memberId);
    }

    @GetMapping("/get-members")
    public GetMemberResponse getMembers(@RequestParam(name = "roomId") String roomId) {
        return chatService.getMembers(roomId);
    }

    @DeleteMapping("/delete-member")
    public RoomResponse deleteMember(
            @RequestParam(name = "roomId") String roomId,
            @RequestParam(name = "memberId") long memberId,
            HttpServletRequest request
    ) {
        return chatService.deleteMember(roomId, memberId, request);
    }

    @GetMapping("/ban-member")
    public RoomResponse banMember(
            @RequestParam(name = "roomId") String roomId,
            @RequestParam(name = "memberId") long memberId,
            HttpServletRequest request) {
        return chatService.banUnbanMember(roomId, memberId, request, true);
    }

    @GetMapping("/unban-member")
    public RoomResponse unbanMember(
            @RequestParam(name = "roomId") String roomId,
            @RequestParam(name = "memberId") long memberId,
            HttpServletRequest request) {
        return chatService.banUnbanMember(roomId, memberId, request, false);
    }
}
