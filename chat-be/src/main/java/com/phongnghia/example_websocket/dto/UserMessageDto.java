package com.phongnghia.example_websocket.dto;

import com.phongnghia.example_websocket.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMessageDto {

    private UUID id;

    private String message;

    private UserEntity user;

}
