class AdminNotification {
    #subscriberURL;
    #notificationURL;
    #onMessageReceived;
    #stompClient;
    #socket;

    constructor(notificationURL = '/admin/ws', subscriberURL = '/admin/notification', onMessageReceived = (payload) => {
        console.log("This is a payload: ", payload);
    }) {
        this.#subscriberURL = subscriberURL;
        this.#notificationURL = notificationURL;
        this.#onMessageReceived = onMessageReceived;
    }

    setOnMessageReceived(onMessageReceived) {
        this.#onMessageReceived = onMessageReceived;
    }

    setSubscriberURL (subscriberURL) {
        this.#subscriberURL = subscriberURL;
    }

    setNotificationURL(notificationURL) {
        this.#notificationURL = notificationURL;
    }

    connect() {
        this.#socket = new SockJS(this.#notificationURL);
        this.#stompClient = Stomp.over(this.#socket);
        this.#stompClient.connect({}, (frame) => {
            this.#stompClient.subscribe(this.#subscriberURL, this.#onMessageReceived);
        }, this.onError);
    }

    onError(error) {
        console.log('Error: ' + error);
    }
}