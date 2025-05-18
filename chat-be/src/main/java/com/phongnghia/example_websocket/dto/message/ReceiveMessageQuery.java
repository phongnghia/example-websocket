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
public class ReceiveMessageQuery {

    private UUID senderId;

    private String senderName;

    private UUID receiverId;

    private String receiverName;

    private String message;

}
