package com.phongnghia.example_websocket.controller;

import com.phongnghia.example_websocket.dto.ReceiveMessageQuery;
import com.phongnghia.example_websocket.dto.SendQueryRequest;
import com.phongnghia.example_websocket.dto.UserDto;
import com.phongnghia.example_websocket.service.SendMessageService;
import com.phongnghia.example_websocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Collections;
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
