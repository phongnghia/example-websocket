package com.phongnghia.example_websocket.controller.websocket;

import com.phongnghia.example_websocket.dto.message.SendQueryRequest;
import com.phongnghia.example_websocket.dto.message.UserMessageDto;
import com.phongnghia.example_websocket.service.message.UserMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class UserMessageController {

    private final UserMessageService m_userMessageService;

    private final SimpMessagingTemplate m_simpMessageTemplate;

    private final String DESTINATION = "/queue/private";

    private final String HISTORY = "history";

    public UserMessageController(
            UserMessageService userMessageService,
            SimpMessagingTemplate simpMessagingTemplate) {
        this.m_userMessageService = userMessageService;
        this.m_simpMessageTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/private_message.send")
    public void sendPrivateMessage(SendQueryRequest sendQueryRequest) {

        m_userMessageService.sendPrivateMessage(sendQueryRequest);

        String dest = String.format(
                "%s/%s/%s/sendFrom/%s",
                DESTINATION,
                HISTORY,
                sendQueryRequest.getReceiverId().toString(),
                sendQueryRequest.getSenderId().toString()
        );
        m_simpMessageTemplate.convertAndSend(
                dest,
                historyOfChat(sendQueryRequest.getSenderId(), sendQueryRequest.getReceiverId())
        );
    }

    @MessageMapping("/private_message.select")
    public void getHistoryOfChat(@Payload SendQueryRequest select) {
        String dest = String.format(
                "%s/%s/%s/sendFrom/%s",
                DESTINATION,
                HISTORY,
                select.getReceiverId().toString(),
                select.getSenderId().toString()
        );

        m_simpMessageTemplate.convertAndSend(
                dest,
                historyOfChat(select.getReceiverId(), select.getSenderId())
        );
    }

    private List<UserMessageDto> historyOfChat(UUID senderId, UUID receiverId) {
        return m_userMessageService.getHistoryOfChat(senderId, receiverId);
    }

}
