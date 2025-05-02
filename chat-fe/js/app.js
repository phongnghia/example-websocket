// 'http://localhost:8082/example-websocket'
// WebSocket connection setup
const socket = new SockJS('http://localhost:8082/example-websocket');
const stompClient = Stomp.over(socket);

// Connect to WebSocket
stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);

    // Subscribe to message topics
    stompClient.subscribe('/topic/messages', function(response) {
        const data = JSON.parse(response.body);
        displayMessageResponse(data);
    });

    stompClient.subscribe('/topic/user', function(response) {
        const data = JSON.parse(response.body);
        displayUserResponse(data);
    });

});

// User Management Functions
document.getElementById('createUserBtn').addEventListener('click', function() {
    const userDto = {
        username: document.getElementById('userName').value,
        fullName: document.getElementById('fullName').value,
        description: document.getElementById('description').value
    };

    if (userDto.username == "") {
        const output = document.getElementById('userOutput');
        output.innerHTML = ''; // Clear previous content
        const userDiv = document.createElement('div');
        userDiv.className = 'user';
        userDiv.innerHTML = `<strong style="color:red">ERROR: Username is required</strong>`
        output.appendChild(userDiv);
        return
    }

    stompClient.send("/app/user.create", {}, JSON.stringify(userDto));
});

document.getElementById('findUserBtn').addEventListener('click', function() {
    const userId = document.getElementById('userId').value;
    stompClient.send("/app/user.id", {}, JSON.stringify(userId));
});

document.getElementById('findAll').addEventListener('click', function() {
    console.log(stompClient.send("/app/user.all", {}, JSON.stringify()));
});

// Message Management Functions
document.getElementById('sendMessageBtn').addEventListener('click', function() {
    const sendQueryRequest = {
        senderId: document.getElementById('senderId').value,
        receiverId: document.getElementById('receiverId').value,
        message: document.getElementById('messageContent').value
    };

    stompClient.send("/app/message.send", {}, JSON.stringify(sendQueryRequest));
});

document.getElementById('getAllMessagesBtn').addEventListener('click', function() {
    stompClient.send("/app/message.all", {}, JSON.stringify({}));
});

// Display functions
function displayUserResponse(data) {
    const output = document.getElementById('userOutput');
    const div = document.createElement('div');
    div.className = 'user';

    if (Array.isArray(data)) {
        // Handle array of users
        output.innerHTML = ''; // Clear previous content
        if (data.length === 0) {
            output.innerHTML = '<div class="user">No users found</div>';
        } else {
            data.forEach(user => {
                const userDiv = document.createElement('div');
                userDiv.className = 'user';
                userDiv.innerHTML = `<strong style="color:blue">User:</strong><br>
                                    <span>ID: ${user.id}</span><br>
                                    <span>Username: ${user.username}</span><br>
                                    <span>Full name: ${user.fullName}</span>`;
                output.appendChild(userDiv);
            });
        }
    }
    else if (data.id) {
        // Handle single user
        div.innerHTML = `<strong style="color:green">Found user:</strong><br>
                        <span>ID: ${data.id}</span><br>
                        <span>Username: ${data.username}</span><br>
                        <span>Full name: ${data.fullName}</span>`;
        output.appendChild(div);
    }
    else if (data.name) {
        // Handle user creation response (assuming different field names)
        div.innerHTML = `<strong>User Created:</strong><br>
                        Name: ${data.name}<br>
                        Email: ${data.email}`;
        output.appendChild(div);
    }
    else {
        // Fallback for other responses
        div.innerHTML = `<strong>Response:</strong> ${JSON.stringify(data)}`;
        output.appendChild(div);
    }

    output.scrollTop = output.scrollHeight;
}

function displayMessageResponse(data) {
    const output = document.getElementById('messageOutput');

    if (Array.isArray(data)) {
        // Handle array of messages
        output.innerHTML = ''; // Clear previous content
        if (data.length === 0) {
            output.innerHTML = '<div class="message">No messages found</div>';
        } else {
            data.forEach(msg => {
                const div = document.createElement('div');
                div.className = 'message';
                div.innerHTML = `<strong>From:</strong> ${msg.senderId}<br>
                                <strong>To:</strong> ${msg.receiverId}<br>
                                <strong>Message:</strong> ${msg.content}<br>
                                <small>${new Date(msg.timestamp).toLocaleString()}</small>`;
                output.appendChild(div);
            });
        }
    } else {
        // Handle single message
        const div = document.createElement('div');
        div.className = 'message';
        div.innerHTML = `<strong>Message Sent:</strong><br>
                          <strong>To:</strong> ${data.receiverId}<br>
                          <strong>Content:</strong> ${data.content}`;
        output.appendChild(div);
    }

    output.scrollTop = output.scrollHeight;
}