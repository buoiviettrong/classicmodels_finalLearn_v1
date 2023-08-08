class ChatSocket {
    #socket;
    #stompClient;
    #userId;
    #chatId;
    #messageHandler;
    #typingHandler;
    #typingTimeout;
    #typingInterval = 1000;
    #joinList = [];

    constructor(socket, userId) {
        this.#socket = socket;
        this.#userId = userId;
        // this.#chatId = chatId;
    }

    connect() {
        this.#stompClient = Stomp.over(this.#socket);
        this.#stompClient.connect({}, this.#onConnect.bind(this));
    }

    #onConnect() {
        // this.#stompClient.subscribe(`/topic/chat/${this.#chatId}`, this.#onMessage.bind(this));
        // this.#stompClient.subscribe(`/topic/chat/${this.#chatId}/typing`, this.#onTyping.bind(this));
        // this.#stompClient.send(`/app/chat/${this.#chatId}/join`, {}, JSON.stringify({userId: this.#userId}));
    }

    #onMessage(message) {
        const messageBody = JSON.parse(message.body);
        this.#messageHandler(messageBody);
    }

    #onTyping(message) {
        const username = JSON.parse(message.body).username;
        this.#typingHandler(username);
    }

    sendMessage(url , message) {
        this.#stompClient.send(url, {}, message);
    }

    addSubscription(subscription, handler) {
        if(this.#joinList.indexOf(subscription) === -1) {
            this.#joinList.push(subscription);
            this.#stompClient.subscribe(subscription, handler);
        }
    }

    onMessage(handler) {
        this.#messageHandler = handler;
    }

    onTyping(handler) {
        this.#typingHandler = handler;
    }

    disconnect() {
        this.#stompClient.disconnect();
    }

    stopTyping() {
        clearTimeout(this.#typingTimeout);
    }

    get username() {
        return this.#userId;
    }

    get chatId() {
        return this.#chatId;
    }

    get typingInterval() {
        return this.#typingInterval;
    }

    set typingInterval(value) {
        this.#typingInterval = value;
    }

    get typingTimeout() {
        return this.#typingTimeout;
    }

    set typingTimeout(value) {
        this.#typingTimeout = value;
    }

    get messageHandler() {
        return this.#messageHandler;
    }

    set messageHandler(value) {
        this.#messageHandler = value;
    }

    get typingHandler() {
        return this.#typingHandler;
    }

    set typingHandler(value) {
        this.#typingHandler = value;
    }

    get socket() {
        return this.#socket;
    }

    set socket(value) {
        this.#socket = value;
    }

    get stompClient() {
        return this.#stompClient;
    }

    set stompClient(value) {
        this.#stompClient = value;
    }
}