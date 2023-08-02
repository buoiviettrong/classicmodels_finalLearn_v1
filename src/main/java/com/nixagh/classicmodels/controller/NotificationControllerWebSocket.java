package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.entity.firebase.NotificationMessage;
import com.nixagh.classicmodels.service.web_socket_service.IWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@EnableWebSocket
@RequestMapping("/api/v1/notification/web-socket")
public class NotificationControllerWebSocket {

    private final IWebSocketService webSocketService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public NotificationMessage sendMessage(
            @Payload NotificationMessage message
    ) {
        return message;
    }

    @PostMapping
    public String sendNotification(@RequestBody NotificationMessage message) {
        return webSocketService.sendGlobalNotification(message);
    }

    @PostMapping("/specific-user/{productName}")
    public String sendNotificationToSpecificUserBy(@RequestBody NotificationMessage message, @PathVariable String productName) {
        return webSocketService.sendNotificationToSpecificUserBy(message, productName);
    }
}
