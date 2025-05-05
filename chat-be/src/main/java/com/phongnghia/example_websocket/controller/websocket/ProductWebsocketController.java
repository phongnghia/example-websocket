package com.phongnghia.example_websocket.controller.websocket;

import com.phongnghia.example_websocket.dto.ProductDto;
import com.phongnghia.example_websocket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProductWebsocketController {

    private final ProductService m_productService;

    @Autowired
    public ProductWebsocketController(ProductService productService) {
        this.m_productService = productService;
    }

    @MessageMapping("/products.getAll")
    @SendTo("/topic/products")
    public List<Optional<ProductDto>> findAll() {
        return m_productService.findAll();
    }

    @MessageMapping("/products.save")
    @SendTo("/topic/products")
    public List<Optional<ProductDto>> addProduct(@Payload ProductDto productDto) {
        m_productService.addProduct(productDto);
        return m_productService.findAll();
    }

    @MessageMapping("/products.findById")
    @SendTo("/topic/products")
    public Optional<ProductDto> findById(@Payload UUID id) {
        return m_productService.findById(id);
    }

}
