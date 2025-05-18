package com.phongnghia.example_websocket.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendQueryRequest {

    private UUID senderId;

    private UUID receiverId;

    private String message;

}
