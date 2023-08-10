package com.nixagh.classicmodels.repository.chat.message;

import com.nixagh.classicmodels.entity.chat.ChatMessage;
import com.nixagh.classicmodels.entity.chat.Room;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.List;

public interface MessageRepository extends BaseRepository<ChatMessage, String> {
    List<ChatMessage> getMessagesByRoom(Room room);

    void deleteAllByRoom(Room room);
}
