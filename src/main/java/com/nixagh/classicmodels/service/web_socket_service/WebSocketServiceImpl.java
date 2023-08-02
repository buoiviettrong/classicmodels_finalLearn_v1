package com.nixagh.classicmodels.service.web_socket_service;

import com.nixagh.classicmodels.entity.firebase.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements IWebSocketService{

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public String sendGlobalNotification(NotificationMessage notificationMessage) {

        simpMessagingTemplate.convertAndSend("/topic/public", notificationMessage);

        return "Notification sent successfully!";
    }

    @Override
    public String sendNotificationToSpecificUserBy(NotificationMessage notificationMessage, String productName) {
        notificationMessage.setData(Map.of("productName", productName));
        // get user by product name in subscription list of product and send notification to user
        List<String> users = Arrays.asList("manager@mail.com", "nixagh2", "nixagh3");
        // send notification to user
        users.forEach(user -> simpMessagingTemplate.convertAndSend("/topic/private/" + user, notificationMessage));
        return "Notification sent successfully!";
    }
}
