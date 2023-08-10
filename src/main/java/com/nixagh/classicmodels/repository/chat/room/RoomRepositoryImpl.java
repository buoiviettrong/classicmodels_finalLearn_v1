package com.nixagh.classicmodels.repository.chat.room;

import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class RoomRepositoryImpl extends BaseRepositoryImpl<Room, String> implements RoomRepository {
    public RoomRepositoryImpl(EntityManager entityManager) {
        super(Room.class, entityManager);
    }

    @Override
    public Optional<Room> getRoomByRoomName(String roomName, long creator) {
        return jpaQueryFactory
                .selectFrom(room)
                .where(room.roomName.eq(roomName).and(room.owner.eq(creator)))
                .stream().findFirst();
    }

    @Override
    public Optional<Room> getRoomByRoomId(String roomId) {
        return jpaQueryFactory
                .selectFrom(room)
                .where(room.roomId.eq(roomId))
                .stream().findFirst();
    }

    @Override
    public List<Room> getRoomsByMembersExists(long memberId) {
        System.out.println("getRoomsByMemberId:: " + memberId);
        return jpaQueryFactory
                .selectFrom(room)
                .leftJoin(room.members, roomMembers)
                .where(roomMembers.id.userId.eq(memberId).and(roomMembers.hasLeft.eq(false)).and(roomMembers.deleted.eq(false)))
                .fetch();
    }

    @Override
    @Modifying
    @Transactional
    public void updateRemoveMember(String roomId, long memberId, boolean hasLeft) {
        JPAUpdateClause query = jpaQueryFactory
                .update(roomMembers)
                .set(roomMembers.hasLeft, hasLeft)
                .where(roomMembers.id.roomId.eq(roomId).and(roomMembers.id.userId.eq(memberId)));
        query.execute();
    }

    @Override
    public List<Tuple> getMemberByRoomId(String roomId) {
        return jpaQueryFactory
                .select(user.id, user.firstName, roomMembers.hasBan)
                .from(room)
                .leftJoin(room.members, roomMembers)
                .leftJoin(roomMembers.user, user)
                .where(room.roomId.eq(roomId).and(roomMembers.hasLeft.eq(false)).and(roomMembers.deleted.eq(false)))
                .fetch();
    }

    @Override
    public Long getOwnerByRoomId(String roomId) {
        return jpaQueryFactory
                .select(room.owner)
                .from(room)
                .where(room.roomId.eq(roomId))
                .fetchOne();
    }

    @Override
    @Modifying
    @jakarta.transaction.Transactional
    public void deleteRoom(Room room_) {
        jpaQueryFactory
                .update(room)
                .set(room.deleted, true)
                .where(room.roomId.eq(room_.getRoomId()))
                .execute();
        jpaQueryFactory
                .update(roomMembers)
                .set(roomMembers.deleted, true)
                .where(roomMembers.id.roomId.eq(room_.getRoomId()))
                .execute();
    }
}
