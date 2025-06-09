package com.phongnghia.example_websocket.service.status;

import com.phongnghia.example_websocket.dto.status.MessageNotificationDto;
import com.phongnghia.example_websocket.mapper.WebSocketConverter;
import com.phongnghia.example_websocket.repository.MessageNotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.HashSet;

@Service
public class MessageNotificationServiceImpl implements MessageNotificationService {

    private final MessageNotificationRepository m_messageNotificationRepository;

    private final WebSocketConverter m_webSocketConverter;

    @Autowired
    public MessageNotificationServiceImpl(MessageNotificationRepository messageNotificationRepository,
                                          WebSocketConverter webSocketConverter) {
        this.m_messageNotificationRepository = messageNotificationRepository;
        this.m_webSocketConverter = webSocketConverter;
    }

    @Override
    public void addNotification(MessageNotificationDto messageNotificationDto) {
        messageNotificationDto.setId(UUID.randomUUID());

        m_messageNotificationRepository.save(m_webSocketConverter.dtoToEntity(messageNotificationDto));

    }

    @Override
    public Set<MessageNotificationDto> listNotificationOfUser(UUID userId) {

        Map<UUID, MessageNotificationDto> notifications = new HashMap<>();

        m_messageNotificationRepository.findByUserId(userId).stream()
                .map(m_webSocketConverter::entityToDto)
                .forEach(dto -> {
                    dto.setId(null);
                    MessageNotificationDto existing = notifications.get(dto.getSenderId());
                    if(existing != null) {
                        existing.setCount(existing.getCount() + 1);
                        notifications.replace(existing.getSenderId(), existing);
                    } else {
                        dto.setCount(1);
                        notifications.put(dto.getSenderId(), dto);
                    }
                });

        return new HashSet<>(notifications.values());
    }

    @Override
    @Transactional
    public void deleteNotification(UUID userId, UUID senderId) {
        m_messageNotificationRepository.deleteAllByUserIdAndSenderId(userId, senderId);
    }
}
