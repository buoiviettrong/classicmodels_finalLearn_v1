package com.nixagh.classicmodels.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetMemberResponse {
    private String roomId;
    private String roomName;
    private Long owner;
    private List<Member> members;

    public record Member(Long memberId, String memberName, boolean isBan) { }
}
