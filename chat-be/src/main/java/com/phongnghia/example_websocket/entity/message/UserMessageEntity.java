package com.phongnghia.example_websocket.entity.message;

import com.phongnghia.example_websocket.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
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

    @Column( name="timestamp", updatable = false)
    @CreatedDate
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sender")
    private UserEntity sender;

}
