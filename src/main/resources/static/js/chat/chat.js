const chatSocket = new ChatSocket(new SockJS('/user/ws'), getUserId());
chatSocket.connect();
const sendMessage = () => {
    const message_ = $('#message');
    const message = message_.val();
    const roomId = $('#room-id').html();
    const sender = getUserId();
    const url = chatURL + '/send-message?roomId=' + roomId;

    if(message === '') {
        return;
    }
    console.log(roomId)
    if (roomId === undefined || roomId === '') {
        alert('Please enter a room');
        return;
    }

    const request = {
        senderId: sender,
        senderName: getUserName(),
        content: message
    }

    const send = callAPI.post(url, request);
    message_.val('');
}

const createRoom = async () => {
    const roomName = $('#roomNameCreate').val();
    const creator = getUserId();
    const url = chatURL + '/create-room';

    const res = await callAPI.post(url, {roomName, creator});
    if(res.success) {
        $('#create-room-alert').empty().append('Created room ' + res['rooms'][0]['roomId']);
        await loadRoomList();
    } else {
        alert(res.message);
    }
}

const joinRoom = async (roomId_) => {
    const roomId = roomId_ || $('#roomIdJoin').val();
    const memberId = getUserId();
    // add member to room
    const url = chatURL + '/join-room?roomId=' + roomId + '&memberId=' + memberId;

    const res = await callAPI.get(url);
    console.log(res)
    if(res.success) {
        // subscribe to room
        const subscription = '/topic/room/' + roomId;
        chatSocket.addSubscription(subscription, (message) => {
            const messageContent = JSON.parse(message.body);
            $('#messages').append(chatComponent(messageContent));
        });
        $('#join-room-alert').empty().append('Joined room ' + roomId);
        await loadRoomList();
        await loadMessages(roomId);
    } else {
        alert(res.message);
    }
}

const loadMessages = async (roomId) => {
    $('#room-id').empty().append(roomId);
    const url = chatURL + '/get-messages?roomId=' + roomId;
    const res = await callAPI.get(url);
    console.log(res);
    if(res.success) {
        const messages = res.messages;
        const messages_ = $('#messages');
        messages_.empty();
        if (messages.length === 0) {
            messages_.append('<div class="text-center">No messages</div>');
        }
        messages.forEach(message => messages_.append(chatComponent(message)));
    } else {
        alert(res.message);
    }
}

function chatComponent(message) {
    return `
        <div class="message">
            <div class="message-header">
                <span class="message-sender font-weight-bold">${message.senderName}</span>
                <span class="message-time">${dateFormatter(message.timestamp)}</span>
            </div>
            <div class="message-content">
                ${message.content}
            </div>
        </div>
    `;
}

const dateFormatter = (date) => {
    const d = new Date(date);
    const day = d.getDate();
    const month = d.getMonth() + 1;
    const year = d.getFullYear();
    const hour = d.getHours();
    const minute = d.getMinutes();
    const second = d.getSeconds();

    return `${day}/${month}/${year} ${hour}:${minute}:${second}`;
}

async function loadRoomList() {
    const rooms_ = $('#rooms');
    const url = chatURL + '/get-rooms?memberId=' + getUserId();
    const rooms = await callAPI.get(url);
    console.log(rooms)
    if(!rooms.success) {
        alert(rooms.message);
        return;
    }
    rooms_.empty();
    if (rooms['rooms'].length === 0) {
        rooms_.append('<div class="text-center">No rooms</div>');
    }
    rooms['rooms'].forEach(room => {
        const roomElement = `
            <div class="room border-top border-bottom" onclick="joinRoom('${room['roomId']}')" style="cursor: pointer">
                <div class="room-name h5">${room.roomName}</div>
            </div>
        `;
        rooms_.append(roomElement);
    });
};

(async () => {
    await loadRoomList();
})();