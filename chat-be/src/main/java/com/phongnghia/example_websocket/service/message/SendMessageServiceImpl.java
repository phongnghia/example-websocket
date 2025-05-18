package com.phongnghia.example_websocket.service.message;

import com.phongnghia.example_websocket.dto.message.ReceiveMessageQuery;
import com.phongnghia.example_websocket.dto.message.SendQueryRequest;
import com.phongnghia.example_websocket.entity.user.UserEntity;
import com.phongnghia.example_websocket.entity.message.UserMessageEntity;
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
    public void sendMessage(SendQueryRequest send) {

        UserMessageEntity userMessageEntity = new UserMessageEntity();
        UserEntity sender = m_userRepository.findById(send.getSenderId()).orElse(null);

        UserEntity receiver = m_userRepository.findById(send.getReceiverId()).orElse(null);

        userMessageEntity.setId(UUID.randomUUID());
        userMessageEntity.setMessage(send.getMessage());
        userMessageEntity.setSender(sender);
        userMessageEntity.setReceiverId(send.getReceiverId());

        m_userMessageRepository.save(userMessageEntity);
    }

    @Override
    public List<Optional<ReceiveMessageQuery>> findMessages() {
        return m_userMessageRepository.findAll().stream().map(userMessage -> {
            Optional<ReceiveMessageQuery> receiveMessageQuery = Optional.of(new ReceiveMessageQuery());
            receiveMessageQuery.ifPresent(receive -> {

                UserEntity receiver = m_userRepository.findById(userMessage.getReceiverId()).orElse(null);

                String receiverName = (receiver != null) ? receiver.getFullName() : null;

                receive.setSenderId(userMessage.getSender().getId());
                receive.setSenderName(userMessage.getSender().getFullName());
                receive.setReceiverId(userMessage.getReceiverId());
                receive.setReceiverName(receiverName);
                receive.setMessage(userMessage.getMessage());
            });
            return receiveMessageQuery;
        }).collect(Collectors.toList());
    }
}
