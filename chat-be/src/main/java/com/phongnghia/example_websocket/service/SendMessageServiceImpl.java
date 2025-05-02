package com.phongnghia.example_websocket.service;

import com.phongnghia.example_websocket.dto.ReceiveMessageQuery;
import com.phongnghia.example_websocket.dto.SendQueryRequest;
import com.phongnghia.example_websocket.entity.UserEntity;
import com.phongnghia.example_websocket.entity.UserMessageEntity;
import com.phongnghia.example_websocket.repository.UserMessageRepository;
import com.phongnghia.example_websocket.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SendMessageServiceImpl implements SendMessageService{

    private final UserMessageRepository m_userMessageRepository;

    private final UserRepository m_userRepository;

    public SendMessageServiceImpl(UserMessageRepository userMessageRepository, UserRepository userRepository){
        this.m_userMessageRepository = userMessageRepository;
        this.m_userRepository = userRepository;
    }

    @Override
    public Optional<ReceiveMessageQuery> sendMessage(SendQueryRequest send) {

        UserMessageEntity userMessageEntity = new UserMessageEntity();
        UserEntity user = m_userRepository.findById(send.getUserId()).orElse(null);

        userMessageEntity.setId(UUID.randomUUID());
        userMessageEntity.setMessage(send.getMessage());
        userMessageEntity.setUser(user);

        m_userMessageRepository.save(userMessageEntity);

        return Optional.of(ReceiveMessageQuery.builder().sender(user.getFullName()).message(send.getMessage()).build());
    }

    @Override
    public List<Optional<ReceiveMessageQuery>> findMessages() {
        return m_userMessageRepository.findAll().stream().map(userMessage -> {
            Optional<ReceiveMessageQuery> receiveMessageQuery = Optional.of(new ReceiveMessageQuery());
            receiveMessageQuery.ifPresent(receive -> {
                receive.setSender(userMessage.getUser().getFullName());
                receive.setMessage(userMessage.getMessage());
            });
            return receiveMessageQuery;
        }).collect(Collectors.toList());
    }
}
