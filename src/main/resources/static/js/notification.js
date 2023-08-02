'use strict';

// <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
// <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

class Socket_ {
    #webSocketURL = '/ws';
    #globalSubscribeURL = `/topic/public`;
    #privateSubscribeURL = `/topic/private/${getEmail()}`;
    #sendURL = '/app/chat.sendMessage';

    constructor(onMessageReceived) {
        this.$onMessageReceived = onMessageReceived
        console.log(this.$onMessageReceived)
        // create socket connection
        this.socket = new SockJS(this.#webSocketURL, {debug: false});
        // create stomp client
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.connect({}, this.onConnected, this.onError);
    }
    // subscribe endpoint
    onConnected = () => {
        this.stompClient.subscribe(this.#globalSubscribeURL, () => {
            this.$onMessageReceived();
        });
        // this.stompClient.subscribe(this.#privateSubscribeURL, this.onPrivateMessageReceived);
    }

    onError = (error) => {
        console.log("this is received error", error);
    }
}
