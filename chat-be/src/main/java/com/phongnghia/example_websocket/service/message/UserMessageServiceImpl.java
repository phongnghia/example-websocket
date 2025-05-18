package com.phongnghia.example_websocket.service.message;

import com.phongnghia.example_websocket.dto.message.UserMessageDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserMessageServiceImpl implements UserMessageService{
    @Override
    public List<UserMessageDto> getUserMessage(UUID id) {
        return List.of();
    }
}