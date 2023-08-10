package com.nixagh.classicmodels.entity.chat;

import com.nixagh.classicmodels.entity.auth.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "room_members")
@Entity
@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomMembers {

    @EmbeddedId
    private RoomMembersId id;

    private boolean hasBan;
    private boolean hasLeft;

    @ManyToOne
    @JoinColumn(name = "roomId")
    @MapsId("roomId")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "userId")
    @MapsId("userId")
    private User user;
}
