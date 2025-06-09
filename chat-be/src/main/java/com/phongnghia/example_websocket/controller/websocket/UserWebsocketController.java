package com.phongnghia.example_websocket.controller.websocket;

import com.phongnghia.example_websocket.dto.ResponseDto;
import com.phongnghia.example_websocket.dto.user.UserDto;
import com.phongnghia.example_websocket.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserWebsocketController {

    private final UserService m_userService;

    private final SimpMessagingTemplate m_simpMessagingTemplate;

    private final String DESTINATION = "/queue/private/";

    @Autowired
    public UserWebsocketController(
            SimpMessagingTemplate simpMessagingTemplate,
            UserService userService) {
        this.m_simpMessagingTemplate = simpMessagingTemplate;
        this.m_userService = userService;
    }

    @MessageMapping("/user.all")
    @SendTo("/topic/user")
    public List<Optional<UserDto>> findAll(){
        return m_userService.findAll();
    }

    @MessageMapping("/user.subscribe")
    public void subscribePrivateMessage(@Payload UUID id){
        m_simpMessagingTemplate.convertAndSend(
                DESTINATION + id.toString(),
                ResponseDto.builder().isSuccess(true).build()
        );
    }

}
