package com.nixagh.classicmodels.service.chat_service;

import com.nixagh.classicmodels.dto.chat.*;
import com.nixagh.classicmodels.entity.auth.User;
import com.nixagh.classicmodels.entity.chat.ChatMessage;
import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.entity.chat.RoomMembers;
import com.nixagh.classicmodels.entity.chat.RoomMembersId;
import com.nixagh.classicmodels.repository.chat.message.MessageRepository;
import com.nixagh.classicmodels.repository.chat.room.RoomRepository;
import com.nixagh.classicmodels.repository.chat.roomMember.IRoomMemberRepository;
import com.nixagh.classicmodels.repository.user.UserRepository;
import com.nixagh.classicmodels.utils.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final IRoomMemberRepository roomMembersRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    @Transactional
    public RoomResponse createRoom(String roomName, long creator) {
        if (roomName == null || roomName.isEmpty())
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

        roomRepository.getRoomByRoomName(roomName, creator).ifPresent(room -> RoomResponse.builder()
                .success(false)
                .message("Room already exists")
        );

        Room room = Room.builder()
                .roomName(roomName)
                .owner(creator)
                .build();

        try {
            room = roomRepository.save(room);
            RoomMembers roomMembers = RoomMembers.builder()
                    .id(new RoomMembersId(room.getRoomId(), creator))
                    .user(user_.get())
                    .room(room)
                    .hasBan(false)
                    .build();
            roomMembersRepository.save(roomMembers);
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
        var room = roomRepository.getRoomByRoomId(roomId);

        if (room.isEmpty()) {
            return RoomResponse.builder()
                    .success(false)
                    .message("Room not found")
                    .build();
        }
        if (room.get().getMembers().stream().anyMatch(member -> user.get().getId().equals(member.getUser().getId()))) {
            roomRepository.updateRemoveMember(roomId, user.get().getId(), false);
            return RoomResponse.builder()
                    .success(true)
                    .message("User already in room")
                    .rooms(List.of(new RoomResponse.Rooms(room.get().getRoomId(), room.get().getRoomName())))
                    .build();
        }

        RoomMembers roomMembers = RoomMembers.builder()
                .id(new RoomMembersId(room.get().getRoomId(), user.get().getId()))
                .room(room.get())
                .user(user.get())
                .hasBan(false)
                .build();
        roomMembersRepository.save(roomMembers);
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

    @Override
    public RoomResponse leaveRoom(String roomId, String username) {
        return null;
    }

    @Override
    public ChatResponse sendMessage(ChatRequest message, String roomId) {
        var room = roomRepository.getRoomByRoomId(roomId);

        if (room.isEmpty()) {
            return ChatResponse.builder()
                    .success(false)
                    .errorMessage("Room not found")
                    .build();
        }
        if (0 != message.getSenderId()) {
            RoomMembers roomMembers = roomMembersRepository.getRoomMembersById(new RoomMembersId(roomId, message.getSenderId()));
            if (roomMembers == null) {
                return ChatResponse.builder()
                        .success(false)
                        .errorMessage("User not in room")
                        .build();
            }
            if (roomMembers.isHasBan()) {
                return ChatResponse.builder()
                        .success(false)
                        .errorMessage("User is banned")
                        .build();
            }
        }

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

    @Override
    public ChatResponse getMessages(String roomId) {
        var room = roomRepository.getRoomByRoomId(roomId);

        if (room.isEmpty()) {
            return ChatResponse.builder()
                    .success(false)
                    .errorMessage("Room not found")
                    .build();
        } else {
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
        List<Room> rooms = roomRepository.getRoomsByMembersExists(memberId);
        return RoomResponse.builder()
                .success(true)
                .rooms(rooms.stream()
                        .map(room -> new RoomResponse.Rooms(room.getRoomId(), room.getRoomName()))
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public RoomResponse deleteRoom(String roomId, long memberId) {
        Room room = roomRepository.getRoomByRoomId(roomId).orElse(null);
        if (room == null) {
            return RoomResponse.builder()
                    .success(false)
                    .message("Room not found")
                    .build();
        }

        try {
            if (room.getOwner().equals(memberId)) {
//                roomMembersRepository.deleteAllByRoom(room);
//                System.out.println("deleted members");
//                messageRepository.deleteAllByRoom(room);
//                System.out.println("deleted messages");
                roomRepository.deleteRoom(room);
                System.out.println("deleted room");
            } else
                roomRepository.updateRemoveMember(roomId, memberId, true);
            this.sendMessage(ChatRequest.builder()
                    .senderId(0)
                    .senderName("System")
                    .content("Room deleted")
                    .build(), roomId);
            return RoomResponse.builder()
                    .success(true)
                    .message("Room deleted successfully")
                    .build();
        } catch (Exception e) {
            return RoomResponse.builder()
                    .success(false)
                    .message("Error deleting room")
                    .build();
        }
    }

    @Override
    public RoomResponse addMember(String roomId, long memberId) {
        Room room = roomRepository.getRoomByRoomId(roomId).orElse(null);
        if (room == null) {
            return RoomResponse.builder()
                    .success(false)
                    .message("Room not found")
                    .build();
        }

        User user = userRepository.findById(memberId).orElse(null);
        if (user == null) {
            return RoomResponse.builder()
                    .success(false)
                    .message("User not found")
                    .build();
        }

        roomRepository.save(room);
        roomMembersRepository.save(RoomMembers.builder()
                .id(new RoomMembersId(roomId, memberId))
                .room(room)
                .user(user)
                .hasLeft(false)
                .build());
        this.sendMessage(ChatRequest.builder()
                .senderId(0)
                .senderName("System")
                .content(user.getUsername() + " joined the room")
                .build(), roomId);

        return RoomResponse.builder()
                .success(true)
                .message("Member added successfully")
                .build();
    }

    @Override
    public GetMemberResponse getMembers(String roomId) {
        Room room = roomRepository.getRoomByRoomId(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        return GetMemberResponse.builder()
                .roomId(roomId)
                .roomName(room.getRoomName())
                .owner(roomRepository.getOwnerByRoomId(roomId))
                .members(roomRepository.getMemberByRoomId(roomId)
                        .stream()
                        .map(member -> new GetMemberResponse.Member(
                                member.get(0, Long.class),
                                member.get(1, String.class),
                                Boolean.TRUE.equals(member.get(2, boolean.class)))
                        )
                        .toList())
                .build();

    }

    @Override
    public RoomResponse deleteMember(String roomId, long memberId, HttpServletRequest request) {
        var result = validateMember(roomId, memberId, request, "delete");
        if (null != result) return result;

        roomRepository.updateRemoveMember(roomId, memberId, true);
        this.sendMessage(
                ChatRequest.builder()
                        .senderId(0L)
                        .senderName("System")
                        .content("Member Id: " + memberId + " have been delete from room ")
                        .build()
                , roomId
        );
        return RoomResponse.builder()
                .success(true)
                .message("Member deleted successfully")
                .build();
    }

    private final JwtUtil jwtUtil;

    @Override
    public RoomResponse banUnbanMember(String roomId, long memberId, HttpServletRequest request, boolean isBan) {
        var result = validateMember(roomId, memberId, request, isBan ? "ban" : "unban");
        if (null != result) return result;

        return updateHasBanRoomMembers(new RoomMembersId(roomId, memberId), isBan);
    }

    private RoomResponse validateMember(String roomId, Long memberId, HttpServletRequest request, String type) {
        User user = jwtUtil.getUserFromHeader(request);
        if (user == null) {
            return RoomResponse.builder()
                    .success(false)
                    .message("User not found")
                    .build();
        }
        Room room = roomRepository.getRoomByRoomId(roomId).orElse(null);
        if (room == null) {
            return RoomResponse.builder()
                    .success(false)
                    .message("Room not found")
                    .build();
        }

        if (!room.getOwner().equals(user.getId())) {
            return RoomResponse.builder()
                    .success(false)
                    .message("User not authorized to " + type + " member")
                    .build();
        }

        if (room.getOwner().equals(memberId)) {
            return RoomResponse.builder()
                    .success(false)
                    .message("Owner cannot be " + type)
                    .build();
        }
        return null;
    }

    private RoomResponse updateHasBanRoomMembers(RoomMembersId roomMembersId, boolean hasBan) {
        try {
            RoomMembers roomMembers = roomMembersRepository.getRoomMembersById(roomMembersId);
            roomMembers.setHasBan(hasBan);
            roomMembersRepository.save(roomMembers);
            String message = hasBan ? "banned" : "unbanned";
            this.sendMessage(
                    ChatRequest.builder()
                            .senderId(0L)
                            .senderName("System")
                            .content("Member Id: " + roomMembersId.getUserId() + " have been " + message + " from room ")
                            .build()
                    , roomMembersId.getRoomId()
            );
            return RoomResponse.builder()
                    .success(true)
                    .message("Member " + message + " successfully")
                    .build();
        } catch (Exception e) {
            return RoomResponse.builder()
                    .success(false)
                    .message("Member not found")
                    .build();
        }
    }
}
