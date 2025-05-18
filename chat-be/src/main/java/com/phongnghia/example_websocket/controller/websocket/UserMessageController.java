package com.phongnghia.example_websocket.controller.websocket;

import com.phongnghia.example_websocket.dto.message.UserMessageDto;
import com.phongnghia.example_websocket.service.message.UserMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class UserMessageController {

    private final UserMessageService m_userMessageService;

    private final SimpMessagingTemplate m_simpMessageTemplate;

    private final String DESTINATION = "/queue/private/";

    public UserMessageController(
            UserMessageService userMessageService,
            SimpMessagingTemplate simpMessagingTemplate) {
        this.m_userMessageService = userMessageService;
        this.m_simpMessageTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/private_message")
    public void sendPrivateMessage(UserMessageDto userMessageDto) {
        String dest = DESTINATION + userMessageDto.getReceiverId();
        m_simpMessageTemplate.convertAndSend(
                dest,
                "test"
        );
    }

}
