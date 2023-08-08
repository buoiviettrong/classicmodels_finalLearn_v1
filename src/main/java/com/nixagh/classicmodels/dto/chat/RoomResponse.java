package com.nixagh.classicmodels.dto.chat;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {
    private boolean success;
    private String message;
    private List<Rooms> rooms;
    public record Rooms(String roomId, String roomName){}
}

