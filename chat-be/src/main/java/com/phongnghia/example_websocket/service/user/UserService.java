package com.phongnghia.example_websocket.service.user;

import com.phongnghia.example_websocket.dto.user.UserDto;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserDto> addUser(UserDto userDto) throws MessagingException;

    Optional<UserDto> findUserById(UUID id);

    List<Optional<UserDto>> findAll();

    Optional<UserDto> findUserByCode(String userCode, boolean isLogin) throws MessagingException;

    boolean isEmailExists(String email);
}
