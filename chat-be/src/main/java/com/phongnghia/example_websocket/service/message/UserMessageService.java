package com.phongnghia.example_websocket.service.message;

import com.phongnghia.example_websocket.dto.message.UserMessageDto;

import java.util.List;
import java.util.UUID;

public interface UserMessageService {
    List<UserMessageDto> getUserMessage(UUID id);
}
