package com.phongnghia.example_websocket.controller;

import com.phongnghia.example_websocket.dto.UserDto;
import com.phongnghia.example_websocket.service.SendMessageService;
import com.phongnghia.example_websocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserWebsocketController {

    private final UserService m_userService;

    @Autowired
    public UserWebsocketController(SendMessageService sendMessageService, UserService userService) {
        this.m_userService = userService;
    }

    @MessageMapping("/user.create")
    @SendTo("/topic/user")
    public void createUser(@Payload UserDto userDto) {
        m_userService.addUser(userDto);
    }

    @MessageMapping("/user.id")
    @SendTo("/topic/user")
    public UserDto findUserById(@Payload UUID id){
        return m_userService.findUserById(id).orElse(null);
    }

    @MessageMapping("/user.all")
    @SendTo("/topic/user")
    public List<Optional<UserDto>> findAll(){
        return m_userService.findAll();
    }

}
