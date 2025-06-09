package com.phongnghia.example_websocket.service.status;

import com.phongnghia.example_websocket.dto.status.MessageNotificationDto;

import java.util.Set;
import java.util.UUID;

public interface MessageNotificationService {

    void addNotification(MessageNotificationDto messageNotificationDto);

    Set<MessageNotificationDto> listNotificationOfUser(UUID userId);

    void deleteNotification(UUID userId, UUID senderId);

}
