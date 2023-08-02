package com.nixagh.classicmodels.entity.firebase;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class NotificationMessage {
    private String registrationToken;
    private String title;
    private String body;
    private String imageUrl;
    private Map<String, String> data;
}
