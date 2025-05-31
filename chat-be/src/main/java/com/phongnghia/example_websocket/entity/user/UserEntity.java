package com.phongnghia.example_websocket.entity.user;

import com.phongnghia.example_websocket.entity.message.UserMessageEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class UserEntity {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    private String fullName;

    private String description;

    @OneToMany( mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<UserMessageEntity> messages;

}
