package com.nixagh.classicmodels.repository.chat.roomMember;

import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.entity.chat.RoomMembers;
import com.nixagh.classicmodels.entity.chat.RoomMembersId;
import com.nixagh.classicmodels.repository.BaseRepository;

public interface IRoomMemberRepository extends BaseRepository<RoomMembers, RoomMembersId> {
    RoomMembers getRoomMembersById(RoomMembersId roomMembersId);

    void deleteAllByRoom(Room room);
}
