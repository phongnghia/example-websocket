package com.phongnghia.example_websocket.entity.status;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;


import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity( name = "notification" )
public class MessageNotification {

    @Id
    private UUID id;

    private UUID userId;

    private UUID senderId;

    private boolean isRead;
 
}
