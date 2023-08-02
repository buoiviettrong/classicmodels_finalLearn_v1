package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.entity.firebase.NotificationMessage;
import com.nixagh.classicmodels.service.firebase_service.IFirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MANAGER')")
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final IFirebaseService firebaseService;

    @PostMapping
    public String sendNotification(@RequestBody NotificationMessage message) {
        return firebaseService.sendNotificationByToken(message);
    }

    @PostMapping("/topic")
    public String sendNotificationToTopic(@RequestBody NotificationMessage message) {
        return firebaseService.sendNotificationToTopic(message);
    }
}
