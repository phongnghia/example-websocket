package com.phongnghia.example_websocket.dto.message;

import com.phongnghia.example_websocket.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMessageDto {

    private UUID id;

    private String message;

    private UUID receiverId;

    private LocalDateTime timestamp;

    private UserDto sender;

}
