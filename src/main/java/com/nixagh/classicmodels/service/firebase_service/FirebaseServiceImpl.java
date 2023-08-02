package com.nixagh.classicmodels.service.firebase_service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.nixagh.classicmodels.entity.firebase.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseServiceImpl implements IFirebaseService {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public String sendNotificationByToken(NotificationMessage notificationMessage) {
        Notification notification = Notification.builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .setImage(notificationMessage.getImageUrl())
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .putAllData(notificationMessage.getData())
                .setToken(notificationMessage.getRegistrationToken())
                .build();

        try {
            return firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Error while sending firebase notification: {}", e.getMessage());
            e.getStackTrace();
            return null;
        }
    }

    @Override
    public String sendNotificationToTopic(NotificationMessage message) {
        Notification notification = Notification.builder()
                .setTitle(message.getTitle())
                .setBody(message.getBody())
                .setImage(message.getImageUrl())
                .build();

        Message message1 = Message.builder()
                .setNotification(notification)
                .putAllData(message.getData())
                .setTopic(message.getRegistrationToken())
                .build();

        try {
            return firebaseMessaging.send(message1);
        } catch (FirebaseMessagingException e) {
            log.error("Error while sending firebase notification: {}", e.getMessage());
            e.getStackTrace();
            return null;
        }
    }
}
