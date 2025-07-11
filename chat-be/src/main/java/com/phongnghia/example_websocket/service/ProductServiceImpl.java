package com.phongnghia.example_websocket.service;

import com.phongnghia.example_websocket.dto.ProductDto;
import com.phongnghia.example_websocket.entity.ProductEntity;
import com.phongnghia.example_websocket.mapper.WebSocketConverter;
import com.phongnghia.example_websocket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository m_productRepository;

    private final WebSocketConverter m_converter;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, WebSocketConverter converter) {
        this.m_productRepository = productRepository;
        this.m_converter = converter;
    }

    @Override
    public List<Optional<ProductDto>> findAll() {
        List<Optional<ProductDto>> list = new ArrayList<>();
        for (ProductEntity product : m_productRepository
                .findAll()) {
            ProductDto productDto = m_converter.entityToDto(product);
            list.add(Optional.ofNullable(productDto));
        }
        return list;
    }

    @Override
    public Optional<ProductDto> findById(UUID id) {
        Optional<ProductEntity> product = m_productRepository.findById(id);
        return Optional.of(m_converter.entityToDto(product.orElse(null)));
    }

    @Override
    public void addProduct(ProductDto productDto) {
        productDto.setId(UUID.randomUUID());
        m_productRepository.save(m_converter.dtoToEntity(productDto));
    }
}
