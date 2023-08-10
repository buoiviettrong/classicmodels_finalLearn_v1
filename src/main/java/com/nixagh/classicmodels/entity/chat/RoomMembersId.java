package com.nixagh.classicmodels.entity.chat;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomMembersId implements Serializable {
    private String roomId;
    private Long userId;
}
