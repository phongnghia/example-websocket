package com.phongnghia.example_websocket.service.message;

import com.phongnghia.example_websocket.dto.message.ReceiveMessageQuery;
import com.phongnghia.example_websocket.dto.message.SendQueryRequest;

import java.util.List;
import java.util.Optional;

public interface SendMessageService {

    Optional<ReceiveMessageQuery> sendMessage(SendQueryRequest send);

    List<Optional<ReceiveMessageQuery>> findMessages();

}
