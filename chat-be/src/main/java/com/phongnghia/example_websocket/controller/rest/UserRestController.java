package com.phongnghia.example_websocket.controller.rest;

import com.phongnghia.example_websocket.dto.ResponseDto;
import com.phongnghia.example_websocket.dto.user.UserDto;
import com.phongnghia.example_websocket.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class UserRestController {

    private final UserService m_userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.m_userService = userService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findUserById(@PathVariable UUID id){
        Optional<UserDto> userDto = m_userService.findUserById(id);

        if (userDto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().isSuccess(false).build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.builder().isSuccess(true).data(userDto).build());
    }

    @GetMapping("/login/{userCode}")
    public ResponseEntity<?> login(@PathVariable String userCode) {
        Optional<UserDto> userDto = m_userService.findUserByCode(userCode, true);

        if (userDto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().isSuccess(false).build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.builder().isSuccess(true).data(userDto).build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        try {
            return m_userService.addUser(userDto)
                    .map(
                            user -> ResponseEntity.status(HttpStatus.OK)
                                    .body(ResponseDto.builder().isSuccess(true).data(user).build()))
                    .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().isSuccess(false).build()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().isSuccess(false).build());
        }
    }

}
