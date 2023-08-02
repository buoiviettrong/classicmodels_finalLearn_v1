'use strict';

class Socket_ {
    #webSocketURL = '/ws';
    #globalSubscribeURL = `/topic/public`;
    #privateSubscribeURL = `/topic/private/${getEmail()}`;
    #sendURL = '/app/chat.sendMessage';

    constructor() {
        // create socket connection
        this.socket = new SockJS(this.#webSocketURL, {debug: false});
        // create stomp client
        this.stompClient = Stomp.over(this.socket);
        // connect to stomp client
        this.stompClient.connect({}, this.onConnected, this.onError);
    }

    // subscribe endpoint
    onConnected = () => {
        this.stompClient.subscribe(this.#globalSubscribeURL, this.onMessageReceived);
        this.stompClient.subscribe(this.#privateSubscribeURL, this.onPrivateMessageReceived);
    }

    onError = (error) => {
        console.log("this is received error", error);
    }

    onMessageReceived = (payload) => {
        const message = JSON.parse(payload.body);
        console.log("this is message::", message);
    }

    onPrivateMessageReceived = (payload) => {
        const message = JSON.parse(payload.body);
        console.log("this is private message::", message);
    }
}

const socket = new Socket_();
