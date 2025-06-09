package com.phongnghia.example_websocket.controller.websocket;

import com.phongnghia.example_websocket.dto.message.SendQueryRequest;
import com.phongnghia.example_websocket.dto.message.UserMessageDto;
import com.phongnghia.example_websocket.dto.status.MessageNotificationDto;
import com.phongnghia.example_websocket.service.message.UserMessageService;
import com.phongnghia.example_websocket.service.status.MessageNotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
public class UserMessageController {

    private final UserMessageService m_userMessageService;

    private final SimpMessagingTemplate m_simpMessageTemplate;

    private final MessageNotificationService m_notificationService;

    private final String QUEUE_PREFIX = "/queue/private";

    private final String HISTORY = "history";

    private final String NOTIFICATION_PREFIX = "/notification";

    public UserMessageController(
            UserMessageService userMessageService,
            SimpMessagingTemplate simpMessagingTemplate,
            MessageNotificationService notificationService) {
        this.m_userMessageService = userMessageService;
        this.m_simpMessageTemplate = simpMessagingTemplate;
        this.m_notificationService = notificationService;
    }

    @MessageMapping("/private_message.send")
    public void sendPrivateMessage(SendQueryRequest sendQueryRequest) {
        if (sendQueryRequest.getMessage() != null) {
            m_userMessageService.sendPrivateMessage(sendQueryRequest);
        }

        String destToSender = String.format(
                "%s/%s/%s/sendFrom/%s",
                QUEUE_PREFIX,
                HISTORY,
                sendQueryRequest.getReceiverId().toString(),
                sendQueryRequest.getSenderId().toString()
        );

        String destToReceiver = String.format(
                "%s/%s/%s/sendFrom/%s",
                QUEUE_PREFIX,
                HISTORY,
                sendQueryRequest.getSenderId().toString(),
                sendQueryRequest.getReceiverId().toString()
        );

        String destToNotificationTopic = String.format(
                "%s/%s",
                NOTIFICATION_PREFIX,
                sendQueryRequest.getReceiverId().toString()
        );

        m_notificationService.addNotification(
                MessageNotificationDto
                        .builder()
                        .userId(sendQueryRequest.getReceiverId())
                        .senderId(sendQueryRequest.getSenderId())
                        .isRead(false)
                        .build()
        );
        sendPrivateMessageToTopic(destToNotificationTopic, m_notificationService.listNotificationOfUser(sendQueryRequest.getReceiverId()));
        sendPrivateMessageToTopic(destToSender, historyOfChat(sendQueryRequest.getSenderId(), sendQueryRequest.getReceiverId()));
        sendPrivateMessageToTopic(destToReceiver, historyOfChat(sendQueryRequest.getSenderId(), sendQueryRequest.getReceiverId()));
    }

    @MessageMapping("/private_message.select")
    public void getHistoryOfChat(@Payload SendQueryRequest select) {
        String dest = String.format(
                "%s/%s/%s/sendFrom/%s",
                QUEUE_PREFIX,
                HISTORY,
                select.getReceiverId().toString(),
                select.getSenderId().toString()
        );
        sendPrivateMessageToTopic(dest, historyOfChat(select.getSenderId(), select.getReceiverId()));
    }

    @MessageMapping("/notification/private.all")
    public void listNotificationOfUser(@Payload UUID userId) {
        String destToNotificationTopic = String.format(
                "%s/%s",
                NOTIFICATION_PREFIX,
                userId.toString()
        );

        sendPrivateMessageToTopic(destToNotificationTopic, listNotification(userId));
    }

    @MessageMapping("/notification/private.read")
    public void removeQueueNotification(@Payload SendQueryRequest request) {
        String dest = String.format(
                "%s/%s",
                NOTIFICATION_PREFIX,
                request.getReceiverId().toString()
        );

        m_notificationService.deleteNotification(request.getReceiverId(), request.getSenderId());

        sendPrivateMessageToTopic(dest, listNotification(request.getReceiverId()));
    }

    private List<UserMessageDto> historyOfChat(UUID senderId, UUID receiverId) {
        return m_userMessageService.getHistoryOfChat(senderId, receiverId);
    }

    private Set<MessageNotificationDto> listNotification(UUID userId) {
        return m_notificationService.listNotificationOfUser(userId);
    }

    private void sendPrivateMessageToTopic(String destination, Object payload) {
        m_simpMessageTemplate.convertAndSend(
                destination,
                payload
        );
    }

}
