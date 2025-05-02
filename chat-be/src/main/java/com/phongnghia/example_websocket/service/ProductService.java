package com.phongnghia.example_websocket.service;

import com.phongnghia.example_websocket.dto.ProductDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    List<Optional<ProductDto>> findAll();

    Optional<ProductDto> findById(UUID id);

    void addProduct(ProductDto productDto);

}
