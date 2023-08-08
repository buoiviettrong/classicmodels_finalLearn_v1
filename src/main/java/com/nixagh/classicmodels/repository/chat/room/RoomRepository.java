package com.nixagh.classicmodels.repository.chat.room;

import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends BaseRepository<Room, String> {
    Optional<Room> getRoomByName(String roomName, long creator);

    Optional<Room> getRoomByName(String roomId);

    Optional<Room> getRoomById(String roomId);

    List<Room> getRoomsByMemberId(long memberId);
}
