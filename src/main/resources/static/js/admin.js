const adminNotification = new AdminNotification();
adminNotification.setOnMessageReceived((message) => {
    const body = message.body;
    showNotification(JSON.parse(body));
});
adminNotification.connect();


const showNotification = (message) => {
    const notification = document.getElementById('notification');
    notification.innerHTML += `
    <div class="alert alert-success alert-dismissible fade show" role="alert" id="alert-success-${message.data.id}">
        <strong>${message.title} </strong>
        ${message.body}
        <button type="button" class="close" data-bs-dismiss="alert" aria-label="Close">X</button>
    </div>
    `;
    setTimeout(() => {
        $(`#alert-success-${message.data.id}`).remove();
    }, 5000);
}
