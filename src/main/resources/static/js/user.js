const logoutBtn = $('#logoutBtn');
logoutBtn.click(async () => {
    callAPI.logout();
});

function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

const token = localStorage.getItem('token');
let user = {
    email: null,
    name: null,
    role: null,
    permissions: [],
    userId: null,
    customerNumber: null,
};
const loadEmail = () => {
    if (token !== null) {
        const tokenObj = parseJwt(token);
        if (tokenObj.exp < Date.now() / 1000) {
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
        $('#dropdownMenuButton').text(tokenObj.email);
        user = {
            email: tokenObj.email,
            name: tokenObj['userName'],
            role: tokenObj.role,
            permissions: tokenObj.permission,
            userId: tokenObj.userId,
            customerNumber: tokenObj.customerNumber,
            ip: tokenObj.ip,
        }
    } else {
        window.location.href = '/login';
    }
}
loadEmail();

const getEmail = () => {
    return user.email;
}

const getIP = () => {
    return user.ip;
}

const getUserId = () => {
    return user.userId;
}

const getUserName = () => {
    return user.name;
}