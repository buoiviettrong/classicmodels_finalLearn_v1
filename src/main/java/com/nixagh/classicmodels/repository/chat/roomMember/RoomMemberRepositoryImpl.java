package com.nixagh.classicmodels.repository.chat.roomMember;

import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.entity.chat.RoomMembers;
import com.nixagh.classicmodels.entity.chat.RoomMembersId;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public class RoomMemberRepositoryImpl extends BaseRepositoryImpl<RoomMembers, RoomMembersId> implements IRoomMemberRepository {
    public RoomMemberRepositoryImpl(EntityManager entityManager) {
        super(RoomMembers.class, entityManager);
    }

    @Override
    public RoomMembers getRoomMembersById(RoomMembersId roomMembersId) {
        return jpaQueryFactory
                .select(roomMembers)
                .from(roomMembers)
                .where(roomMembers.id.eq(roomMembersId).and(roomMembers.hasLeft.eq(false)))
                .fetchFirst();
    }

    @Override
    @Modifying
    @Transactional
    public void deleteAllByRoom(Room room) {
        jpaQueryFactory
                .delete(roomMembers)
                .where(roomMembers.room.roomId.eq(room.getRoomId()))
                .execute();
    }

}
