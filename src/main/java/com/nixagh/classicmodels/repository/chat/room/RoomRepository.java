package com.nixagh.classicmodels.repository.chat.room;

import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.repository.BaseRepository;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends BaseRepository<Room, String> {
    Optional<Room> getRoomByRoomName(String roomName, long creator);

    Optional<Room> getRoomByRoomId(String roomId);

    List<Room> getRoomsByMembersExists(long memberId);

    void updateRemoveMember(String roomId, long memberId, boolean hasLeft);

    List<Tuple> getMemberByRoomId(String roomId);

    Long getOwnerByRoomId(String roomId);

    void deleteRoom(Room room);
}
