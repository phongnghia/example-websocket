package com.phongnghia.example_websocket.dto.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageNotificationDto {

    private UUID id;

    private UUID userId;

    private UUID senderId;

    private boolean isRead;

    private int count;

}
