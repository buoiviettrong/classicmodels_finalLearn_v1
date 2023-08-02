package com.nixagh.classicmodels.service.firebase_service;

import com.nixagh.classicmodels.entity.firebase.NotificationMessage;

public interface IFirebaseService {

    String sendNotificationByToken(NotificationMessage notificationMessage);

    String sendNotificationToTopic(NotificationMessage message);
}
