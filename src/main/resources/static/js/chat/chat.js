const chatSocket = new ChatSocket(new SockJS('/user/ws'), getUserId());
chatSocket.connect();

async function sendMessage() {
    const message_ = $('#message');
    const message = message_.val();
    const roomId = $('#room-id').html();
    const sender = getUserId();
    const url = chatURL + '/send-message?roomId=' + roomId;

    if (message === '') {
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

    const send = await callAPI.post(url, request);
    message_.val('');
    if (!send.success)
        alert(send['errorMessage'])

}

async function createRoom() {
    const roomName = $('#roomNameCreate').val();
    const creator = getUserId();
    const url = chatURL + '/create-room';

    const res = await callAPI.post(url, {roomName, creator});
    if (res.success) {
        $('#create-room-alert').empty().append('Created room ' + res['rooms'][0]['roomId']);
        await loadRoomList();
    } else {
        alert(res.message);
    }
}

async function joinRoom(roomId_) {
    const roomId = roomId_ || $('#roomIdJoin').val();
    const memberId = getUserId();
    // add member to room
    const url = chatURL + '/join-room?roomId=' + roomId + '&memberId=' + memberId;

    const res = await callAPI.get(url);
    console.log(res)
    if (res.success) {
        // subscribe to room
        const subscription = '/topic/room/' + roomId;
        chatSocket.addSubscription(subscription, (message) => {
            const messageContent = JSON.parse(message.body);
            $('#messages').append(chatComponent(messageContent));
            if (0 === messageContent.sender)
                loadRoomMembers(roomId);
        });
        $('#join-room-alert').empty().append('Joined room ' + roomId);
        await loadRoomList();
        await loadMessages(roomId);
        await loadRoomMembers(roomId);
    } else {
        alert(res.message);
    }
}

async function loadMessages(roomId) {
    $('#room-id').empty().append(roomId);
    const url = chatURL + '/get-messages?roomId=' + roomId;
    const res = await callAPI.get(url);
    console.log(res);
    if (res.success) {
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

function dateFormatter(date) {
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
    if (!rooms.success) {
        alert(rooms.message);
        return;
    }
    rooms_.empty();
    if (rooms['rooms'].length === 0) {
        rooms_.append('<div class="text-center">No rooms</div>');
    }
    rooms['rooms'].forEach(room => {
        const roomElement = `
            <div class="row">
                <div class="room border-top border-bottom col" onclick="joinRoom('${room['roomId']}')" style="cursor: pointer">
                    <div class="room-name h5">${room.roomName}</div>
                </div>
                <button class="btn btn-sm btn-outline-secondary col-1" onclick="action.leaveRoom('${room['roomId']}')">Leave</button>
            </divrow>
        `;
        rooms_.append(roomElement);
    });
}

function loadRoomMembers(roomId) {
    addMembers(roomId);
}

function addMembers(roomId) {
    const x = $('#members');
    callAPI.get(chatURL + '/get-members?roomId=' + roomId).then(res => {
        const members = res.members;
        let members_ = '';
        members.forEach(member => {
            members_ += `
            <div class="room-member row">
                <span class="room-member-name col">${member['memberName']}</span>
                <div class="dropdown col">
                    <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            ...
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <div class="dropdown-item" onclick="action.removeMember('${member['memberId']}')">Remove</div>
                        ${member['isBan']
                ? `<div class="dropdown-item" onclick="action.unban('${member['memberId']}')">Unban</div>`
                : `<div class="dropdown-item" onclick="action.ban('${member['memberId']}')">Ban</div>`}
                        <div class="dropdown-item" onclick="action.viewProfile('${member['memberId']}')">View profile</div>
                        <div class="dropdown-item" onclick="action.sendMessage('${member['memberId']}')">Send message</div>
                    </div>
                </div>
            </div>`;
        });
        x.empty().append(members_);
    });
}

const action = {
    removeMember: (memberId) => {
        const roomId = $('#room-id').text();
        const url = chatURL + '/delete-member?memberId=' + memberId + '&roomId=' + roomId;
        callAPI.delete(url).then(res => {
            if (res.success) {
                loadRoomMembers(roomId);
                alert('Removed member ' + memberId);
            } else {
                alert(res.message);
            }
        });
    },
    ban: async (memberId) => {
        const roomId = $('#room-id').text();
        const url = chatURL + '/ban-member?memberId=' + memberId + '&roomId=' + roomId;
        const res = await callAPI.get(url);
        if (res.success) {
            loadRoomMembers(roomId);
            alert('Banned member ' + memberId);
        } else {
            alert(res.message);
        }
    },
    unban: async (memberId) => {
        const roomId = $('#room-id').text();
        const url = chatURL + '/unban-member?memberId=' + memberId + '&roomId=' + roomId;
        const res = await callAPI.get(url);
        if (res.success) {
            loadRoomMembers(roomId);
            alert('Unbanned member ' + memberId);
        } else {
            alert(res.message);
        }
    },
    viewProfile: (memberId) => {

    },
    sendMessage: (memberId) => {

    },
    leaveRoom: async (roomId) => {
        const res = await callAPI.delete(chatURL + '/delete-room?roomId=' + roomId + '&memberId=' + getUserId());
        console.log(res);
    }
};

(async () => {
    await loadRoomList();
})();