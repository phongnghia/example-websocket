package com.phongnghia.example_websocket.repository;

import com.phongnghia.example_websocket.entity.status.MessageNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageNotificationRepository extends JpaRepository<MessageNotification, UUID> {

    List<MessageNotification> findByUserId(UUID userId);

    void deleteAllByUserIdAndSenderId(UUID userId, UUID senderId);

}
