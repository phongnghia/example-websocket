// Connect websocket
console.log("Connecting to websocket ...")
export const socket = new SockJS('http://localhost:8082/example-websocket');
export const apiURL = "http://localhost:8082/rest"
export const stompClient = Stomp.over(socket);

export function generateSessionId() {
    return 'session-' + Math.random().toString(36).substr(2, 9) + '-' + Date.now();
}