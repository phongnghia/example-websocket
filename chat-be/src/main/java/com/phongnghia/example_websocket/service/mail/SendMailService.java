package com.phongnghia.example_websocket.service.mail;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface SendMailService {

    void sendMailWithTemplate(String to, String subject, String templateName,
                              Map<String, Object> variables) throws MessagingException;

}
