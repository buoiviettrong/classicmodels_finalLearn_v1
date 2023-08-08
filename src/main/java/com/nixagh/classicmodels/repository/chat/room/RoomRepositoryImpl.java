package com.nixagh.classicmodels.repository.chat.room;

import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class RoomRepositoryImpl extends BaseRepositoryImpl<Room, String> implements RoomRepository {
    public RoomRepositoryImpl(EntityManager entityManager) {
        super(Room.class, entityManager);
    }

    @Override
    public Optional<Room> getRoomByName(String roomName, long creator) {
        return jpaQueryFactory
                .selectFrom(room)
                .where(room.roomName.eq(roomName).and(room.owner.eq(creator)))
                .stream().findFirst();
    }

    @Override
    public Optional<Room> getRoomByName(String roomName) {
        return jpaQueryFactory
                .selectFrom(room)
                .where(room.roomName.eq(roomName))
                .stream().findFirst();
    }

    @Override
    public Optional<Room> getRoomById(String roomId) {
        return jpaQueryFactory
                .selectFrom(room)
                .where(room.roomId.eq(roomId))
                .stream().findFirst();
    }

    @Override
    public List<Room> getRoomsByMemberId(long memberId) {
        System.out.println("getRoomsByMemberId:: " + memberId);
        return jpaQueryFactory
                .selectFrom(room)
                .where(room.members.any().id.eq(memberId))
                .fetch();
    }
}
