function loadNotification(response) {

    if (Array.isArray(response)) {
        if (response.length === 0) {
            return;
        }
        renderNotification(response);
    } else {
        renderNotification(response);
    }

}

function renderNotification(notifications) {

    if (notifications.length === 0) {
        return;
    }

    notifications.forEach(item => {
        const userElement = document.querySelector(`[data-user-id="${item.senderId}"]`);

        if (userElement) {
            const notification = userElement.querySelector(".user-item-notification");
            if (notification) {
                notification.innerHTML = `${item.count}`;
            } else {
                const newElement = document.createElement('div');
                newElement.className = "user-item-notification";
                newElement.innerHTML = `${item.count}`;

                userElement.appendChild(newElement);
            }
        }
    });
}
