package com.phongnghia.example_websocket.service.message;

import com.phongnghia.example_websocket.dto.message.SendQueryRequest;
import com.phongnghia.example_websocket.dto.message.UserMessageDto;
import com.phongnghia.example_websocket.entity.message.UserMessageEntity;
import com.phongnghia.example_websocket.entity.user.UserEntity;
import com.phongnghia.example_websocket.mapper.WebSocketConverter;
import com.phongnghia.example_websocket.repository.UserMessageRepository;
import com.phongnghia.example_websocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserMessageServiceImpl implements UserMessageService{

    private final UserRepository m_userRepository;

    private final UserMessageRepository m_userMessageRepository;

    private final WebSocketConverter m_webSocketConverter;

    @Autowired
    public UserMessageServiceImpl(UserRepository userRepository,
                                  UserMessageRepository userMessageRepository,
                                  WebSocketConverter webSocketConverter) {
        this.m_userRepository = userRepository;
        this.m_userMessageRepository = userMessageRepository;
        this.m_webSocketConverter = webSocketConverter;
    }

    @Override
    public List<UserMessageDto> getHistoryOfChat(UUID senderId, UUID receiverId) {
        List<UserMessageDto> list = m_userMessageRepository
                .findAll()
                .stream()
                .filter(userMessageEntity ->
                        ((userMessageEntity.getReceiverId() == receiverId)
                        && (userMessageEntity.getSender().getId() == senderId)))
                .map(m_webSocketConverter::entityToDto)
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public void sendPrivateMessage(SendQueryRequest sendQueryRequest) {
        UserEntity sender = m_userRepository.findById(sendQueryRequest.getSenderId()).orElse(null);
        m_userMessageRepository.save(
                UserMessageEntity
                        .builder()
                        .message(sendQueryRequest.getMessage())
                        .receiverId(sendQueryRequest.getReceiverId())
                        .sender(sender)
                        .build()
        );
    }
}