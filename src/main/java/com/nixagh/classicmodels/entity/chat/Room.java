package com.nixagh.classicmodels.entity.chat;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roomId;

    private String roomName;
    private Long owner;
    private boolean deleted;

    @OneToMany
    @JoinColumn(name = "roomId")
    private Set<RoomMembers> members;

    @OneToMany(mappedBy = "room")
    private List<ChatMessage> messages;
}
