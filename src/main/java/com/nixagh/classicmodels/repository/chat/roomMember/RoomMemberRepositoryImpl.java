package com.nixagh.classicmodels.repository.chat.roomMember;

import com.nixagh.classicmodels.entity.chat.RoomMembers;
import com.nixagh.classicmodels.entity.chat.RoomMembersId;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

public class RoomMemberRepositoryImpl extends BaseRepositoryImpl<RoomMembers, RoomMembersId> implements IRoomMemberRepository {
    public RoomMemberRepositoryImpl(EntityManager entityManager) {
        super(RoomMembers.class, entityManager);
    }

    @Override
    public RoomMembers getRoomMembersById(RoomMembersId roomMembersId) {
        return jpaQueryFactory
                .select(roomMembers)
                .from(roomMembers)
                .where(roomMembers.id.eq(roomMembersId))
                .fetchFirst();
    }
}
