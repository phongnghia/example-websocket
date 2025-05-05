package com.phongnghia.example_websocket.repository;

import com.phongnghia.example_websocket.entity.message.UserMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessageEntity, UUID> {
}
