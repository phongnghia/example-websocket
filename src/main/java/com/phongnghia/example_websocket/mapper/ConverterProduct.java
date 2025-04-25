package com.phongnghia.example_websocket.mapper;

import com.phongnghia.example_websocket.dto.ProductDto;
import com.phongnghia.example_websocket.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConverterProduct {

    ConverterProduct INSTANCE = Mappers.getMapper(ConverterProduct.class);

    ProductDto entityToDto(ProductEntity product);

    ProductEntity dtoToEntity(ProductDto productDto);

}
