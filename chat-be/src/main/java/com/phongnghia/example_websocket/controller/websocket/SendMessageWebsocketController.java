package com.phongnghia.example_websocket.controller.websocket;

import com.phongnghia.example_websocket.dto.message.ReceiveMessageQuery;
import com.phongnghia.example_websocket.dto.message.SendQueryRequest;
import com.phongnghia.example_websocket.service.message.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SendMessageWebsocketController {

    private final SendMessageService m_sendMessageService;

    @Autowired
    public SendMessageWebsocketController(SendMessageService sendMessageService) {
        this.m_sendMessageService = sendMessageService;
    }

    @MessageMapping("/app/message.send")
    @SendTo("/topic/message")
    public void send(@Payload SendQueryRequest sendQueryRequest){
        m_sendMessageService.sendMessage(sendQueryRequest);
    }

    @MessageMapping("/app/message.all")
    @SendTo("/topic/message")
    public List<ReceiveMessageQuery> findAll(){
        return m_sendMessageService.findMessages().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

}
