package com.phongnghia.example_websocket.service.common;

import com.phongnghia.example_websocket.dto.VerifyCodeDto;
import com.phongnghia.example_websocket.entity.code.VerifyCodeEntity;
import com.phongnghia.example_websocket.mapper.WebSocketConverter;
import com.phongnghia.example_websocket.repository.VerifyCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService{

    private final VerifyCodeRepository m_verifyCodeRepository;

    private final WebSocketConverter m_webSocketConverter;

    @Autowired
    public CommonServiceImpl(VerifyCodeRepository verifyCodeRepository, WebSocketConverter webSocketConverter) {
        this.m_verifyCodeRepository = verifyCodeRepository;
        this.m_webSocketConverter = webSocketConverter;
    }

    @Override
    public VerifyCodeDto findCode(String code) {

        VerifyCodeEntity entity = m_verifyCodeRepository.findByCode(code);

        if (entity == null || !entity.isActive()) {
            return null;
        }

        entity.setActive(false);
        m_verifyCodeRepository.save(entity);
        return m_webSocketConverter.entityToDto(entity);
    }
}
