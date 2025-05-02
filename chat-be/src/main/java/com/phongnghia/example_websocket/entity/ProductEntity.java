package com.phongnghia.example_websocket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity (name = "product")
public class ProductEntity {

    @Id
    private UUID id;

    private String productName;

    private String price;

    private String description;
}
