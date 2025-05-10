package com.phongnghia.example_websocket.service.user;

import com.phongnghia.example_websocket.dto.user.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserDto> addUser(UserDto userDto);

    Optional<UserDto> findUserById(UUID id);

    List<Optional<UserDto>> findAll();

    void sendMessageToSubscriberUser(UUID id, Object message);
}
