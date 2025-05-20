import { apiURL, stompClient, avatarUser, generateSessionId } from './global.js';
import { selectChatUser } from './message.js';

let users = [];
let data = [];
let newUserId = null;

// ========== //

export let currentUser = {};

// First controller
const authContainer = document.getElementById('authContainer');
const appContainer = document.getElementById('appContainer');
/// Tab
const accessTab = document.getElementById('accessTab');
const createTab = document.getElementById('createTab');
/// Button
const accessBtn = document.getElementById('accessBtn');
const createBtn = document.getElementById('createBtn');
const logoutBtn = document.getElementById('userLogout');

// Second controller
const userList = document.getElementById('userList');
const messageInput = document.getElementById('messageInput');
const currentUserAvatar = document.getElementById('currentUserAvatar');
const currentUserName = document.getElementById('currentUserName');
const currentUserId = document.getElementById('currentUserId');

// WebSocket connection
stompClient.connect({}, function (frame) {
    loadMainPage();
});

function loadMainPage(){
    let getLocalStorageItem = JSON.parse(localStorage.getItem("user"));

    if (getLocalStorageItem && getLocalStorageItem != null) currentUser = getLocalStorageItem;

    if (currentUser && currentUser.id) {
        stompClient.subscribe("/topic/user", function (response) {
            users = JSON.parse(response.body);
            displayUserResponse(users);
        });

        // stompClient.subscribe(`/queue/private/${currentUser.id}`, function(response){
        //     let checkStatus = JSON.parse(response.body);
        //     console.log(checkStatus);
        // });

        currentUserAvatar.textContent = avatarUser(currentUser.fullName);
        currentUserName.textContent = currentUser.fullName;
        currentUserId.textContent = currentUser.id;

        authContainer.classList.add('hidden');
        appContainer.classList.remove('hidden');

        console.log(`Accessed with user ${currentUser.fullName}`);

        subscribePrivateMessage(currentUser.id);
        findAllUsers();
    }
}

function findAllUsers() {
    stompClient.send("/app/user.all", {}, JSON.stringify());
    return 0;
}

function subscribePrivateMessage(id){
    stompClient.send("/app/user.subscribe", {}, JSON.stringify(id));
}

function findUserById(id) {
    fetch(apiURL + "/find/" + id, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            currentUser = data.data;
            localStorage.setItem("user", JSON.stringify(currentUser));
            loadMainPage();
        })
        .catch(error => {
            showError("Oops! Something went wrong on our end!");
            console.error(error);
            accessTab.click();
        })
}

function createNewUser(newUser) {
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
        .catch(error => {
            showError(error);
            console.error(error);
            createTab.click();
        })
}

// ========== //

// Access function
accessBtn.addEventListener('click', () => {
    const userId = document.getElementById('accessUserId').value;

    if (!userId) {
        showError("UserId is required");
        return 1;
    }

    if (!isValidUUID(userId)) {
        showError("UserId is not valid. It must be a UUID string");
        return 1;
    }

    findUserById(userId);
});

// Create function
createBtn.addEventListener('click', () => {
    const username = document.getElementById('createUsername').value;
    const fullName = document.getElementById('createFullName').value;
    const description = document.getElementById('createDescription').value;

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

    createNewUser(newUser);
});

// Logout button
logoutBtn.addEventListener('click', () => {
    localStorage.clear();
    window.location.reload();
});


// ========== //

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
                <!--<div class="user-item-id">${user.id}</div>-->
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