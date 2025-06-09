import { apiURL, stompClient, avatarUser } from "./global.js";
import { currentUser } from "./user.js";

export let currentChatUser = {};
let chatSubscription = null;
const messageInput = document.getElementById('messageInput');
const sendButton = document.getElementById('sendButton');
const chatMessages = document.getElementById('chatMessages');

stompClient.connect({}, function (frame) {
    if (currentChatUser && currentChatUser.id) {
        loadChatBox(currentUser.id, currentChatUser.id);
    }
});

function readMessage(obj) {
    const parentElement = document.querySelector(`[data-user-id="${obj.senderId}"]`);
    if (parentElement) {
        const userItemNotification = parentElement.querySelector(".user-item-notification");
        if (userItemNotification) userItemNotification.remove();
    }
    stompClient.send("/app/notification/private.read", {}, JSON.stringify(obj));
}

function loadChatBox(senderId, receiverId) {
    if (chatSubscription) {
        chatSubscription.unsubscribe();
    }
    chatSubscription = stompClient.subscribe(`/queue/private/history/${receiverId}/sendFrom/${senderId}`, function (response) {
        let messages = JSON.parse(response.body);

        let obj = {
            receiverId: senderId,
            senderId: receiverId
        }

        readMessage(obj);

        loadChatHistory(receiverId, messages);
    });
}

export function selectChatUser(receiver) {
    const senderId = currentUser.id;
    const receiverId = receiver.id;
    currentChatUser.id = receiver.id;
    currentChatUser.fullName = receiver.fullName;

    loadChatBox(senderId, receiverId);

    const selectHistory = {
        senderId: senderId,
        receiverId: receiverId
    }

    chatPartnerAvatar.textContent = avatarUser(receiver.fullName);
    chatPartnerName.textContent = receiver.fullName;
    document.querySelector('.chat-header-status').textContent = "@" + receiver.username;

    stompClient.send("/app/private_message.select", {}, JSON.stringify(selectHistory));

    messageInput.disabled = false;
    sendButton.disabled = false;
    messageInput.placeholder = 'Type your message here...';
}

function loadChatHistory(userId, messages) {
    chatMessages.textContent = '';
    
    if (!userId || !messages || !Array.isArray(messages) || messages.length == 0) {
        const notFoundElement = document.createElement('div');
        notFoundElement.innerHTML = `
            <div class="welcome-message" id="welcomeMessage">
                <p>Message not sent yet!</p>
            </div>
        `;
        chatMessages.appendChild(notFoundElement);
        return;
    }

    const historyMessages = messages.map(message => ({
        receiverId: message.receiverId,
        senderId: message.sender.id,
        content: message.message || message.content,
        timestamp: message.timestamp
    }));

    historyMessages.sort((x, y) => {
        return (new Date(x.timestamp)) - (new Date(y.timestamp));
    });

    const fragment = document.createDocumentFragment();

    let dateTimeHeader = null;
    
    historyMessages.forEach(message => {
        const messageTimestamp = formatDateHeader(message.timestamp);
        if (messageTimestamp != dateTimeHeader) {
            dateTimeHeader = messageTimestamp;
            const dateHeader = document.createElement('div');
            dateHeader.innerHTML = `
                <div class="message-time-header">${dateTimeHeader}</div>
            `
            chatMessages.appendChild(dateHeader);
        }
        const messageElement = renderMessage(message);
        if (messageElement) {
            fragment.appendChild(messageElement);
        }
    });
    
    chatMessages.appendChild(fragment);

    setTimeout(() => {
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }, 0);
}

function renderMessage(message) {
    const isCurrentUser = message.senderId === currentUser.id;
    const senderName = isCurrentUser ? 'You' : currentChatUser.fullName;
    const timestamp = formatTimestamp(message.timestamp);
    const dataHeader = formatDateHeader(message.timestamp);

    const messageElement = document.createElement('div');
    messageElement.className = `message ${isCurrentUser ? 'sent' : 'received'}`;

    messageElement.innerHTML = `
        <div class="message-info">
            <div class="message-sender">${senderName}</div>
            <div class="message-time">${timestamp}</div>
        </div>
        <div class="message-bubble">${message.content}</div>
    `;

    chatMessages.appendChild(messageElement);
}

sendButton.addEventListener('click', () => {
    const content = messageInput.value.trim();
    if (!content || !currentChatUser) return;

    const sendMessage = {
        senderId: currentUser.id,
        receiverId: currentChatUser.id,
        message: content
    }

    stompClient.send("/app/private_message.send", {}, JSON.stringify(sendMessage));

    renderMessage(sendMessage);

    messageInput.value = '';

    chatMessages.scrollTop = chatMessages.scrollHeight;
});

function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

function formatDateHeader(timestamp) {
    const today = new Date();
    const date = new Date(timestamp);
    
    if (date.toDateString() === today.toDateString()) {
        return 'Today';
    }
    
    const yesterday = new Date(today);
    yesterday.setDate(yesterday.getDate() - 1);
    if (date.toDateString() === yesterday.toDateString()) {
        return 'Yesterday';
    }
    
    return date.toLocaleDateString([], { year: 'numeric', month: 'long', day: 'numeric' });
}

messageInput.addEventListener('input', function () {
    this.style.height = 'auto';
    this.style.height = (this.scrollHeight) + 'px';
});