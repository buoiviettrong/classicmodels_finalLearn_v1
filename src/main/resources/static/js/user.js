const user = localStorage.getItem('user');
if (user !== null) {
    const userObj = JSON.parse(user);
    const userElement = $('#dropdownMenuButton');
    userElement.val(userObj.email);
    userElement.html(userObj.email);
}

const logoutBtn = $('#logoutBtn');
logoutBtn.click(async () => {
    const data = await callAPI.get('/api/v1/auth/logout');
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    window.location.href = '/login';
});