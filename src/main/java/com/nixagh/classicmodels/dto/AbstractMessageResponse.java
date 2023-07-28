package com.nixagh.classicmodels.dto;

import com.nixagh.classicmodels.dto.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractMessageResponse {
    private List<Message> messages = new ArrayList<>();
}
