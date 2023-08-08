package com.nixagh.classicmodels.service.chat_service;

import com.nixagh.classicmodels.dto.chat.ChatRequest;
import com.nixagh.classicmodels.dto.chat.ChatResponse;
import com.nixagh.classicmodels.dto.chat.GetRoomResponse;
import com.nixagh.classicmodels.dto.chat.RoomResponse;
import com.nixagh.classicmodels.entity.auth.User;
import com.nixagh.classicmodels.entity.chat.ChatMessage;
import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.repository.chat.message.MessageRepository;
import com.nixagh.classicmodels.repository.chat.room.RoomRepository;
import com.nixagh.classicmodels.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Override
    public RoomResponse createRoom(String roomName, long creator) {
        if(roomName == null || roomName.isEmpty())
            return RoomResponse.builder()
                .success(false)
                .message("Room name is empty")
                .build();

        Optional<User> user_ = userRepository.findById(creator);

        if (user_.isEmpty()) {
            return RoomResponse.builder()
                    .success(false)
                    .message("User not found")
                    .build();
        }

        roomRepository.getRoomByName(roomName, creator).ifPresent(room -> RoomResponse.builder()
                .success(false)
                .message("Room already exists")
        );

        Room room = Room.builder()
                .roomName(roomName)
                .owner(creator)
                .members(Set.of(user_.get()))
                .build();

        try {
            roomRepository.save(room);
        } catch (RuntimeException e) {
            return RoomResponse.builder()
                    .success(false)
                    .message("Error while creating room")
                    .build();
        }

        return RoomResponse.builder()
                .success(true)
                .message("Room created successfully")
                .rooms(List.of(new RoomResponse.Rooms(room.getRoomId(), room.getRoomName())))
                .build();
    }

    @Override
    public RoomResponse joinRoom(String roomId, long username) {
        var user = userRepository.findById(username);
        if (user.isEmpty()) {
            return RoomResponse.builder()
                    .success(false)
                    .message("User not found")
                    .build();
        }

        System.out.println("roomId:: " + roomId);
        var room = roomRepository.getRoomById(roomId);

        if (room.isEmpty()) {
            return RoomResponse.builder()
                    .success(false)
                    .message("Room not found")
                    .build();
        } else {
            if(room.get().getMembers().contains(user.get())) {
                return RoomResponse.builder()
                        .success(true)
                        .message("User already in room")
                        .rooms(List.of(new RoomResponse.Rooms(room.get().getRoomId(), room.get().getRoomName())))
                        .build();
            }
            room.ifPresent(value -> value.getMembers().add(user.get()));
            roomRepository.save(room.get());

            this.sendMessage(ChatRequest.builder()
                    .senderId(0)
                    .senderName("System")
                    .content(user.get().getUsername() + " joined the room")
                    .build(), roomId);
            return RoomResponse.builder()
                    .success(true)
                    .message("Joined room successfully")
                    .rooms(List.of(new RoomResponse.Rooms(room.get().getRoomId(), room.get().getRoomName())))
                    .build();
        }
    }

    @Override
    public RoomResponse leaveRoom(String roomId, String username) {
        return null;
    }

    @Override
    public ChatResponse sendMessage(ChatRequest message, String roomId) {
        var room = roomRepository.getRoomById(roomId);

        if (room.isEmpty()) {
            return ChatResponse.builder()
                    .success(false)
                    .errorMessage("Room not found")
                    .build();
        }
        else {
            ChatMessage chatMessage = ChatMessage.builder()
                    .sender(message.getSenderId())
                    .senderName(message.getSenderName())
                    .content(message.getContent())
                    .room(room.get())
                    .timestamp(System.currentTimeMillis())
                    .build();

            room.get().getMessages().add(chatMessage);
            roomRepository.save(room.get());
            messageRepository.save(chatMessage);
            simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, chatMessage);
            return ChatResponse.builder()
                    .success(true)
                    .messages(room.get().getMessages())
                    .build();
        }
    }

    @Override
    public ChatResponse getMessages(String roomId) {
        var room = roomRepository.getRoomById(roomId);

        if (room.isEmpty()) {
            return ChatResponse.builder()
                    .success(false)
                    .errorMessage("Room not found")
                    .build();
        }
        else {
            var messages = messageRepository.getMessagesByRoom(room.get());
            return ChatResponse.builder()
                    .success(true)
                    .messages(messages)
                    .build();
        }
    }

    @Override
    public GetRoomResponse getRoom(String roomId) {
        return null;
    }

    @Override
    public RoomResponse getRooms(long memberId) {
        List<Room> rooms = roomRepository.getRoomsByMemberId(memberId);
        return RoomResponse.builder()
                .success(true)
                .rooms(rooms.stream()
                        .map(room -> new RoomResponse.Rooms(room.getRoomId(), room.getRoomName()))
                        .toList())
                .build();
    }
}
