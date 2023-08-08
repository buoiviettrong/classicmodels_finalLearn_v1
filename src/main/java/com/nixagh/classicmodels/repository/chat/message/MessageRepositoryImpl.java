package com.nixagh.classicmodels.repository.chat.message;

import com.nixagh.classicmodels.entity.chat.ChatMessage;
import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

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
}
