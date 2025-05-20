package com.phongnghia.example_websocket.mapper;

import com.phongnghia.example_websocket.dto.ProductDto;
import com.phongnghia.example_websocket.dto.message.UserMessageDto;
import com.phongnghia.example_websocket.dto.user.UserDto;
import com.phongnghia.example_websocket.entity.ProductEntity;
import com.phongnghia.example_websocket.entity.message.UserMessageEntity;
import com.phongnghia.example_websocket.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WebSocketConverter {

    WebSocketConverter INSTANCE = Mappers.getMapper(WebSocketConverter.class);

    ProductDto entityToDto(ProductEntity product);

    ProductEntity dtoToEntity(ProductDto productDto);

    UserEntity dtoToEntity(UserDto userDto);

    UserDto entityToDto(UserEntity user);

    UserMessageDto entityToDto(UserMessageEntity userMessageEntity);

    UserMessageEntity dtoToEntity(UserMessageDto userMessageDto);

}
