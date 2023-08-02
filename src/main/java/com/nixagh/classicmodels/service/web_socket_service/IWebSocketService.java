package com.nixagh.classicmodels.service.web_socket_service;

import com.nixagh.classicmodels.entity.firebase.NotificationMessage;

public interface IWebSocketService {
    String sendGlobalNotification(NotificationMessage notificationMessage);
    String sendNotificationToSpecificUserBy(NotificationMessage notificationMessage, String productName);
}
