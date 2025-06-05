import { apiURL, stompClient, avatarUser, generateSessionId } from './global.js';
import { selectChatUser } from './message.js';

let users = [];
let data = [];
let newUserCode = null;

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
const currentUserAvatar = document.getElementById('currentUserAvatar');
const currentUserName = document.getElementById('currentUserName');
const currentUserId = document.getElementById('currentUserId');

// WebSocket connection
stompClient.connect({}, function (frame) {
    loadMainPage();
});

function loadMainPage() {
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

function subscribePrivateMessage(id) {
    stompClient.send("/app/user.subscribe", {}, JSON.stringify(id));
}

function findUserById(userCode) {
    fetch(apiURL + "/login/" + userCode, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            if (!data.success) {
                showError("Oops! We couldn't find and account with that user code!");
                accessTab.click();
            }
            currentUser = data.data;
            localStorage.setItem("user", JSON.stringify(currentUser));
            loadMainPage();
        })
        .catch(error => {
            showError("Oops! Something went wrong on our end!");
            // console.error(error);
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
            if (!data.success) {
                if (data.message != null) {
                    showError(data.message);
                    return;
                }
                showError("The username is already in use. Please try another.");
                return;
            }
            newUserCode = data.data.userCode;
            document.getElementById('accessUserId').value = newUserCode;
            showSuccess("Registration successful!. \nYour code should be remembered");
            accessTab.click();
        })
        .catch(error => {
            showError(error);
            // console.error(error);
            createTab.click();
        })
}

// ========== //

// Access function
accessBtn.addEventListener('click', () => {
    const userId = document.getElementById('accessUserId').value;

    if (!userId) {
        showError("User code is required");
        return 1;
    }

    if (!isValidUUID(userId)) {
        showError("Invalid user code. The code must start with 'CAP_' and be exactly 8 characters long (e.g., 'CAP_1234')");
        return 1;
    }

    findUserById(userId);
});

// Create function
createBtn.addEventListener('click', () => {
    const username = document.getElementById('createUsername').value;
    const email = document.getElementById('createEmail').value;
    const fullName = document.getElementById('createFullName').value;
    const description = document.getElementById('createDescription').value;

    console.log(`Creating user: ${username}, ${fullName}, ${description}`);

    if (!username) {
        showError("Username is required");
        createTab.click();
        return 1;
    }

    if (!email) {
        showError("Email is required");
        createTab.click();
        return 1;
    }

    if (!validateEmail(email)) {
        showError("Email is invalid");
        createTab.click();
        return 1;
    }

    var newUser = {
        username: username,
        email: email,
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