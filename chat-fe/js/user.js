import { apiURL, stompClient, generateSessionId } from './global.js';

let users = [];
let messages = [];
let data = [];
let newUserId = null;

// ========== //

let currentUser = null;
let currentChatUser = null;

// First controller
const authContainer = document.getElementById('authContainer');
const appContainer = document.getElementById('appContainer');
/// Tab
const accessTab = document.getElementById('accessTab');
const createTab = document.getElementById('createTab');
/// Form
const accessForm = document.getElementById('accessForm');
const createForm = document.getElementById('createForm');
/// Button
const accessBtn = document.getElementById('accessBtn');
const createBtn = document.getElementById('createBtn');

// Second controller
const userList = document.getElementById('userList');
const chatMessages = document.getElementById('chatMessages');
const messageInput = document.getElementById('messageInput');
const sendButton = document.getElementById('sendButton');
const currentUserAvatar = document.getElementById('currentUserAvatar');
const currentUserName = document.getElementById('currentUserName');
const currentUserId = document.getElementById('currentUserId');

const chatPartnerAvatar = document.getElementById('chatPartnerAvatar');
const chatPartnerName = document.getElementById('chatPartnerName');

// WebSocket connection
stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    console.log(findAllUsers());
    stompClient.subscribe('/topic/user', function (response) {
        users = JSON.parse(response.body);
        if (!Array.isArray(users)) {
            if (users.success === true) {
                currentUser = users.data;

                currentUserAvatar.textContent = currentUser.fullName
                                                    .split(" ")
                                                    .map(word => word[0])
                                                    .join('')
                                                    .slice(0, 2);
                currentUserName.textContent = currentUser.fullName;
                currentUserId.textContent = currentUser.id;

                authContainer.classList.add('hidden');
                appContainer.classList.remove('hidden');

                console.log(`Accessed with user ${currentUser.fullName}`);
                findAllUsers();
            } else {
                showError("User not found");
                return 1;
            }
        }
        displayUserResponse(users);
    });
    stompClient.subscribe('/topic/messages', function (response) {
        messages = JSON.parse(response.body);
    });
});

function findAllUsers() {
    stompClient.send("/app/user.all", {}, JSON.stringify());
    return 0;
}

function findUserById(id) {
    stompClient.send('/app/user.id', {}, JSON.stringify(id));
}

// ========== //

// Access function
accessBtn.addEventListener('click', () => {
    const userId = document.getElementById('accessUserId').value;
    // const username = document.getElementById('accessUsername').value;

    if (!userId) {
        showError("UserId is required");
        return 1;
    }

    checkingUser(userId);
});

// Create function
createBtn.addEventListener('click', () => {
    const username = document.getElementById('createUsername').value;
    const fullName = document.getElementById('createFullName').value;
    const description = document.getElementById('createDescription').value;

    createUser(username, fullName, description);
});

// ========== //

// Create user
function createUser(username, fullName, description) {
    console.log(`Creating user: ${username}, ${fullName}, ${description}`);

    if (!username) {
        showError("Username is required");
        createTab.click();
        return 1;
    }

    var newUser = {
        username: username,
        fullName: fullName,
        description: description
    }

    fetch(apiURL + "/add", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUser)
    })
    .then(response => response.json())
    .then(data => {
        newUserId = data.data.id;
        document.getElementById('accessUserId').value = newUserId;
        showSuccess("Registration successful!. \nYour ID should be remembered");
        accessTab.click();
    })
    .catch(error =>  {
        showError("Oops! That username is taken");
        createTab.click();
    })
}

// Access checking
function checkingUser(userId) {
    console.log(`Checking user: ${userId}`);

    currentUser = {
        id: userId
    };

    loadUsers(currentUser);
}

// Load user from database
function loadUsers(user) {
    // Get user
    const userId = user.id;

    if (!userId) {
        window.location.reload();
        showError("UserId is required");
        return 1;
    }

    if (!isValidUUID(userId)) {
        showError("UserId is not valid. It must be a UUID string");
        return 1;
    }

    findUserById(userId);
}

// Render user list
function renderUserList() {
    userList.innerHTML = '';

    if (data.length === 0) {
        userList.innerHTML = '<div class="no-users">No users found</div>';
        return;
    }

    users.forEach(user => {
        // Skip current user
        if (user.id === currentUser.id) return;

        const userElement = document.createElement('div');
        userElement.className = 'user-item';
        userElement.dataset.userId = user.id;

        const avatar = user.fullName
            .split(" ")
            .map(word => word[0])
            .join('')
            .slice(0, 2);

        userElement.innerHTML = `
            <div class="user-item-avatar">${avatar}</div>
            <div class="user-item-info">
                <div class="user-item-name">${user.fullName}</div>
                <div class="user-item-username">@${user.username}</div>
                <div class="user-item-id">${user.id}</div>
            </div>
        `;

        userElement.addEventListener('click', () => {
            selectChatUser(user);
        });

        userList.appendChild(userElement);
    });
}

// Handle user updates from WebSocket

function displayUserResponse(users) {
    if (Array.isArray(users)) {
        if (users.length === 0) {
            return;
        }
        // Full user list update
        if (currentUser && currentUser.id) {
            data = users.filter(user => user.id !== currentUser.id);
        } else {
            data = users;
        }
        renderUserList();
    } else {
        renderUserList();
    }
}

// Auto-resize textarea
messageInput.addEventListener('input', function () {
    this.style.height = 'auto';
    this.style.height = (this.scrollHeight) + 'px';
});