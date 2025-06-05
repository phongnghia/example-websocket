package com.phongnghia.example_websocket.service.user;

import com.phongnghia.example_websocket.dto.user.UserDto;
import com.phongnghia.example_websocket.entity.user.UserEntity;
import com.phongnghia.example_websocket.mapper.WebSocketConverter;
import com.phongnghia.example_websocket.repository.UserRepository;
import com.phongnghia.example_websocket.utils.GenerateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository m_userRepository;

    private final WebSocketConverter m_converter;

    private final String PREFIX_NAME = "CAP";

    private final SimpMessagingTemplate m_simpMessagingTemplate;

    @Autowired
    public UserServiceImpl(WebSocketConverter converter,
                           UserRepository userRepository,
                           SimpMessagingTemplate simpMessagingTemplate){
        this.m_converter = converter;
        this.m_userRepository = userRepository;
        this.m_simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Optional<UserDto> addUser(UserDto userDto) {
        userDto.setId(UUID.randomUUID());

        String code;

        while (true) {
            code = String.format("%s_%s", PREFIX_NAME, GenerateCode.generateRandomCode());

            Optional<UserDto> checkCode = Optional.ofNullable(m_converter.entityToDto(m_userRepository.findByUserCode(code)));

            if (checkCode.isEmpty()) {
                break;
            }
        }

        userDto.setUserCode(code);
        UserEntity user = m_converter.dtoToEntity(userDto);
        m_userRepository.save(user);
        return Optional.of(userDto);
    }

    @Override
    public Optional<UserDto> findUserById(UUID id) {
        UserDto userDto = m_converter.entityToDto(m_userRepository.findById(id).map(userEntity -> {
            userEntity.setUserCode(null);
            return userEntity;
        }).orElse(null));
        return Optional.ofNullable(userDto);
    }

    @Override
    public List<Optional<UserDto>> findAll(){
        return m_userRepository.findAll()
                .stream()
                .map(user -> {
                    user.setUserCode(null);
                    return Optional.of(m_converter.entityToDto(user));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> findUserByCode(String userCode, boolean isLogin) {

        UserEntity user = m_userRepository.findByUserCode(userCode);

        if (isLogin && user != null) user.setUserCode(null);

        return Optional.ofNullable(m_converter
                .entityToDto(user)
        );
    }

    @Override
    public boolean isEmailExists(String email) {
        return m_userRepository.findByEmail(email) != null;
    }

}
