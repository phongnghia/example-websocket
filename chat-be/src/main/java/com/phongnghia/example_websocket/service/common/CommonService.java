package com.phongnghia.example_websocket.service.common;

import com.phongnghia.example_websocket.dto.VerifyCodeDto;

public interface CommonService {
    VerifyCodeDto findCode(String code);
}
