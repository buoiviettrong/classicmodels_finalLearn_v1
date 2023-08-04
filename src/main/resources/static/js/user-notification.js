class UserNotification {
    #socket;
    #stompClient;

    #subscriberURL;
    #notificationURL;

    #onMessageReceived;

    constructor(subscriberURL, notificationURL, onMessageReceived) {
        this.#subscriberURL = subscriberURL || `/user/notifications/${getEmail()}/${getIP()}`;
        this.#notificationURL = notificationURL || '/user/ws';
        this.#onMessageReceived = onMessageReceived || function (message) {
            console.log(message);
        };
    }

    connect() {
        this.#socket = new SockJS(this.#notificationURL);
        this.#stompClient = Stomp.over(this.#socket);
        this.#stompClient.connect(
            {},
            (frame) => this.#stompClient.subscribe(this.#subscriberURL, this.#onMessageReceived),
            this.onError
        );
    }

    onError(error) {
        console.log(error);
    }

    set onMessageReceived(onMessageReceived) {
        this.#onMessageReceived = onMessageReceived;
    }
}

const userNotification = new UserNotification();
userNotification.onMessageReceived = function (message) {
    const messageBody = JSON.parse(message.body);

    const title = messageBody.title;
    const body = messageBody.body;
    const id = messageBody.data.id;

    $('#notification').append(
        `<div class="alert alert-info alert-dismissible fade show" role="alert" id="alert-${id}">
            <strong>${title}</strong> ${body}
            <button type="button" class="btn btn-close" data-bs-dismiss="alert" aria-label="Close">X</button>
        </div>`
    );

    setTimeout(() => {
        $(`#alert-${id}`).alert('close');
    }, 5000);
};
userNotification.connect();