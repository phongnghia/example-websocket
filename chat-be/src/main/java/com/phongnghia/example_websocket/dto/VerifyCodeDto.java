package com.phongnghia.example_websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyCodeDto {
    private UUID id;

    private String code;

    private boolean active;
}
