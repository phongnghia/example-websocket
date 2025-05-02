package com.phongnghia.example_websocket.service;

import com.phongnghia.example_websocket.dto.ReceiveMessageQuery;
import com.phongnghia.example_websocket.dto.SendQueryRequest;

import java.util.List;
import java.util.Optional;

public interface SendMessageService {

    Optional<ReceiveMessageQuery> sendMessage(SendQueryRequest send);

    List<Optional<ReceiveMessageQuery>> findMessages();

}
