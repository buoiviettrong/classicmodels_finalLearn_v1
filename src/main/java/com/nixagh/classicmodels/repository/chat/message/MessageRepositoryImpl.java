package com.nixagh.classicmodels.repository.chat.message;

import com.nixagh.classicmodels.entity.chat.ChatMessage;
import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class MessageRepositoryImpl extends BaseRepositoryImpl<ChatMessage, String> implements MessageRepository {
    public MessageRepositoryImpl(EntityManager entityManager) {
        super(ChatMessage.class, entityManager);
    }

    @Override
    public List<ChatMessage> getMessagesByRoom(Room room) {
        return jpaQueryFactory
                .selectFrom(chatMessage)
                .where(chatMessage.room.eq(room))
                .orderBy(chatMessage.timestamp.asc())
                .fetch();
    }

    @Override
    @Modifying
    @Transactional
    public void deleteAllByRoom(Room room) {
        jpaQueryFactory
                .delete(chatMessage)
                .where(chatMessage.room.roomId.eq(room.getRoomId()))
                .execute();
    }
}
