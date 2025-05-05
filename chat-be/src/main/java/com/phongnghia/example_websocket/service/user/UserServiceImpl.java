package com.phongnghia.example_websocket.service.user;

import com.phongnghia.example_websocket.dto.user.UserDto;
import com.phongnghia.example_websocket.entity.user.UserEntity;
import com.phongnghia.example_websocket.mapper.WebSocketConverter;
import com.phongnghia.example_websocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository m_userRepository;

    private final WebSocketConverter m_converter;

    @Autowired
    public UserServiceImpl(WebSocketConverter converter, UserRepository userRepository){
        this.m_converter = converter;
        this.m_userRepository = userRepository;
    }

    @Override
    public Optional<UserDto> addUser(UserDto userDto) {
        userDto.setId(UUID.randomUUID());
        UserEntity user = m_converter.dtoToEntity(userDto);
        m_userRepository.save(user);
        return Optional.of(userDto);
    }

    @Override
    public Optional<UserDto> findUserById(UUID id) {
        UserDto userDto = m_converter.entityToDto(m_userRepository.findById(id).orElse(null));
        return Optional.ofNullable(userDto);
    }

    @Override
    public List<Optional<UserDto>> findAll(){
        return m_userRepository.findAll()
                .stream()
                .map(user -> Optional.of(m_converter.entityToDto(user)))
                .collect(Collectors.toList());
    }
}
