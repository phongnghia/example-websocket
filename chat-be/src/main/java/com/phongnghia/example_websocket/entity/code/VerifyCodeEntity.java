package com.phongnghia.example_websocket.entity.code;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.UUID;
import jakarta.persistence.Id;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "verify_code")
@Builder
public class VerifyCodeEntity {

    @Id
    private UUID id;

    private String code;

    private boolean active;

}
