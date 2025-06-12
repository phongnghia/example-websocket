package com.phongnghia.example_websocket.controller.rest;

import com.phongnghia.example_websocket.dto.ResponseDto;
import com.phongnghia.example_websocket.dto.VerifyCodeDto;
import com.phongnghia.example_websocket.dto.user.UserDto;
import com.phongnghia.example_websocket.service.common.CommonService;
import com.phongnghia.example_websocket.service.user.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class UserRestController {

    private final UserService m_userService;

    private final CommonService m_commonService;

    @Autowired
    public UserRestController(UserService userService, CommonService commonService) {
        this.m_userService = userService;
        this.m_commonService = commonService;
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
    public ResponseEntity<?> login(@PathVariable String userCode) throws MessagingException {
        Optional<UserDto> userDto = m_userService.findUserByCode(userCode, true);

        if (userDto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().isSuccess(false).build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.builder().isSuccess(true).data(userDto).build());
    }

    @GetMapping("/verify/{verifyCode}")
    public ResponseEntity<?> verifyCode(@PathVariable String verifyCode) {

        VerifyCodeDto dto = m_commonService.findCode(verifyCode);

        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().isSuccess(false).message("Unverified").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.builder().isSuccess(true).message("Verified").build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        try {

            if (m_userService.isEmailExists(userDto.getEmail())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(
                                ResponseDto.builder().isSuccess(false).message("This email is already in use. Please try another").build()
                        );
            }

            return m_userService.addUser(userDto)
                    .map(
                            user -> ResponseEntity.status(HttpStatus.OK)
                                    .body(ResponseDto.builder().isSuccess(true).data(user).build()))
                    .orElse(
                            ResponseEntity
                                    .status(HttpStatus.BAD_REQUEST)
                                    .body(ResponseDto.builder().isSuccess(false).build()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().isSuccess(false).build());
        }
    }

}
