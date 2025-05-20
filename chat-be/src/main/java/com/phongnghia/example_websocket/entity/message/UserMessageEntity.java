package com.phongnghia.example_websocket.entity.message;

import com.phongnghia.example_websocket.entity.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "message")
@Builder
public class UserMessageEntity {

    @Id
    private UUID id;

    private String message;

    private UUID receiverId;

    @ManyToOne
    @JoinColumn(name = "sender")
    private UserEntity sender;

}
