package com.phongnghia.example_websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto<T> {
    private boolean isSuccess;

    private T data;

    private T message;
}
