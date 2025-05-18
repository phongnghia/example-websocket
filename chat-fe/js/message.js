import { apiURL, stompClient, avatarUser } from "./global.js";
import { currentUser } from "./user.js";

let currentChatUser;

export function selectChatUser(receiver){
    const senderId = currentUser.id;
    const receiverId = receiver.id;

    chatPartnerAvatar.textContent = avatarUser(receiver.fullName);
    chatPartnerName.textContent = receiver.fullName;
    document.querySelector('.chat-header-status').textContent = "@"+receiver.username;

    messageInput.disabled = false;
    sendButton.disabled = false;
    messageInput.placeholder = 'Type your message here...';

    loadChatHistory(receiver.id);
}

function loadChatHistory(userId) {

    chatMessages.innerHTML = '';

    setTimeout(() => {
        const messages = [
            {
                id: 'msg-1',
                senderId: userId,
                content: 'Hello there!',
                timestamp: new Date(Date.now() - 3600000).toISOString()
            },
            {
                id: 'msg-2',
                senderId: currentUser.id,
                content: 'Hi! How are you?',
                timestamp: new Date(Date.now() - 1800000).toISOString()
            },
            {
                id: 'msg-3',
                senderId: userId,
                content: 'I\'m doing well, thanks for asking!',
                timestamp: new Date().toISOString()
            }
        ];

        messages.forEach(message => {
            renderMessage(message);
        });

        chatMessages.scrollTop = chatMessages.scrollHeight;
    }, 300);
}

function renderMessage(message) {
    const isCurrentUser = message.senderId === currentUser.id;
    const senderName = isCurrentUser ? 'You' : currentChatUser.fullName;
    const timestamp = new Date(message.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    
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

function sendMessage() {
    const content = messageInput.value.trim();
    if (!content || !currentChatUser) return;

    const message = {
        senderId: currentUser.id,
        receiverId: currentChatUser.id,
        content: content,
        timestamp: new Date().toISOString()
    };

    if (stompClient) {
        stompClient.send("/app/message.send", {}, JSON.stringify(message));
    }

    renderMessage(message);

    messageInput.value = '';

    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function handleUserUpdate(data) {
    if (Array.isArray(data)) {
        users = data.filter(user => user.id !== currentUser.id);
        renderUserList();
    } else {
        const index = users.findIndex(user => user.id === data.id);
        if (index >= 0) {
            users[index] = data;
        } else if (data.id !== currentUser.id) {
            users.push(data);
        }
        renderUserList();
    }
}

function handleMessageUpdate(data) {
    if (Array.isArray(data)) {
        chatMessages.innerHTML = '';
        data.forEach(message => {
            renderMessage(message);
        });
    } else {
        if (data.senderId === currentChatUser?.id || data.receiverId === currentUser.id) {
            renderMessage(data);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
    }
}

messageInput.addEventListener('input', function() {
    this.style.height = 'auto';
    this.style.height = (this.scrollHeight) + 'px';
});