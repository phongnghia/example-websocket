// Validate UUID
function isValidUUID(uuid) {
    const regex = /^CAP_[0-9A-Z]{8}$/;
    return regex.test(uuid);
}

function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

// Tab switching
accessTab.addEventListener('click', () => {
    accessTab.classList.add('active');
    createTab.classList.remove('active');
    accessForm.classList.remove('hidden');
    createForm.classList.add('hidden');
});

// Create tab
createTab.addEventListener('click', () => {
    createTab.classList.add('active');
    accessTab.classList.remove('active');
    createForm.classList.remove('hidden');
    accessForm.classList.add('hidden');
});

function showError(message) {
    const notificationOverlay = document.getElementById('notificationOverlay');
    const notificationContent = document.getElementById('notificationContent');

    notificationContent.textContent = message;
    notificationOverlay.classList.add('active');

    document.getElementById('notificationClose').addEventListener('click', hideError);
    document.getElementById('notificationButton').addEventListener('click', hideError);

    notificationOverlay.addEventListener('click', function (e) {
        if (e.target === notificationOverlay) {
            hideError();
        }
    });
}

function hideError() {
    document.getElementById('notificationOverlay').classList.remove('active');
    window.location.reload();
}

function showSuccess(message) {
    const overlay = document.getElementById('successOverlay');
    const content = document.getElementById('successContent');

    content.textContent = message;
    overlay.classList.add('active');

    overlay.querySelector('.notification-close').onclick = () => hideSuccess();
    overlay.querySelector('.notification-button').onclick = () => hideSuccess();

    overlay.onclick = (e) => {
        if (e.target === overlay) hideSuccess();
    };
}

function hideSuccess() {
    document.getElementById('successOverlay').classList.remove('active');
}