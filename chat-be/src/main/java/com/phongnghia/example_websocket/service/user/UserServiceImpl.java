package com.phongnghia.example_websocket.service.user;

import com.phongnghia.example_websocket.dto.user.UserDto;
import com.phongnghia.example_websocket.entity.user.UserEntity;
import com.phongnghia.example_websocket.entity.code.VerifyCodeEntity;
import com.phongnghia.example_websocket.mapper.WebSocketConverter;
import com.phongnghia.example_websocket.repository.UserRepository;
import com.phongnghia.example_websocket.repository.VerifyCodeRepository;
import com.phongnghia.example_websocket.service.mail.SendMailService;
import com.phongnghia.example_websocket.utils.CommonStringName;
import com.phongnghia.example_websocket.utils.GenerateCode;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository m_userRepository;

    private final VerifyCodeRepository m_verifyCodeRepository;

    private final WebSocketConverter m_converter;

    private final SendMailService m_sendMailService;

    private final String LOGIN_SUBJECT = "Verify Your Account - One-Time Code Enclosed";

    private final String USER_CODE_SUBJECT = "Here’s Your Access Code – Use It Now";

    private final String PREFIX_NAME = "CAP";

    private final SimpMessagingTemplate m_simpMessagingTemplate;

    @Autowired
    public UserServiceImpl(WebSocketConverter converter,
                           UserRepository userRepository,
                           VerifyCodeRepository verifyCodeRepository,
                           SimpMessagingTemplate simpMessagingTemplate,
                           SendMailService sendMailService){
        this.m_converter = converter;
        this.m_userRepository = userRepository;
        this.m_verifyCodeRepository = verifyCodeRepository;
        this.m_simpMessagingTemplate = simpMessagingTemplate;
        this.m_sendMailService = sendMailService;
    }

    @Override
    public Optional<UserDto> addUser(UserDto userDto) throws MessagingException {
        userDto.setId(UUID.randomUUID());

        String code;

        while (true) {
            code = String.format("%s_%s", PREFIX_NAME, GenerateCode.generateRandomCode()).toUpperCase();

            Optional<UserDto> checkCode = Optional.ofNullable(m_converter.entityToDto(m_userRepository.findByUserCode(code)));

            if (checkCode.isEmpty()) {
                break;
            }
        }

        userDto.setUserCode(code);
        UserEntity user = m_converter.dtoToEntity(userDto);
        m_userRepository.save(user);

        Map<String, Object> variables = new HashMap<>();

        variables.put("username", userDto.getUsername());
        variables.put("userCode", code);

        sendMailWithCode(userDto.getEmail(), USER_CODE_SUBJECT, variables, CommonStringName.SEND_USER_CODE_TEMPLATE.getStr());
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
    public Optional<UserDto> findUserByCode(String userCode, boolean isLogin) throws MessagingException {

        UserEntity user = m_userRepository.findByUserCode(userCode);

        if (user == null) {
            return Optional.empty();
        }

        String verifyCode = GenerateCode.generateRandomCode().toUpperCase();
        VerifyCodeEntity verifyCodeEntity = VerifyCodeEntity.builder().id(UUID.randomUUID()).code(verifyCode).active(true).build();
        m_verifyCodeRepository.save(verifyCodeEntity);

        Map<String, Object> variables = new HashMap<>();

        variables.put("username", user.getUsername());
        variables.put("verifyCode", verifyCode);

        sendMailWithCode(user.getEmail(), LOGIN_SUBJECT, variables, CommonStringName.SEND_VERIFY_CODE_TEMPLATE.getStr());

        if (isLogin) user.setUserCode(null);

        return Optional.ofNullable(m_converter
                .entityToDto(user)
        );
    }

    @Override
    public boolean isEmailExists(String email) {
        return m_userRepository.findByEmail(email) != null;
    }

    private void sendMailWithCode(String email, String subject, Map<String, Object> variables, String template) throws MessagingException {

        m_sendMailService.sendMailWithTemplate(
                email,
                subject,
                template,
                variables);
    }

}
