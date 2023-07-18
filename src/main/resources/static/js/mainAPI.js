const headers = {
    "Content-Type": "application/json",
    "Accept": "application/json",
};

const checkStatus = (response) => {
    if (response.status === 401) {
        alert("Please login to continue");
        window.location.href = "/login";
    }
    return response;
}

const callAPI = {
    base: (method, url, data) => {
        if (localStorage.getItem("token") !== null)
            headers["Authorization"] = "Bearer " + localStorage.getItem("token");
        let init = {
            method: method,
            headers: headers,
        };
        if (data != null) init["body"] = JSON.stringify(data);

        return fetch(host + url, init)
            .then((response) => checkStatus(response))
            .then((response) => response.json());
    },

    post: (url, data) => {
        return callAPI.base("POST", url, data);
    },
    get: (url, data) => {
        return callAPI.base("GET", url, data);
    },
    put: (url, data) => {
        return callAPI.base("PUT", url, data);
    },
    delete: (url) => {
        return callAPI.base("DELETE", url);
    },
    logout: () => {
        headers["Authorization"] = "Bearer " + localStorage.getItem("token");
        let init = {
            method: 'GET',
            headers: headers,
        };
        fetch(host + '/api/v1/auth/logout', init).then((response) => {
            localStorage.removeItem("token");
            window.location.href = "/login";
        }).catch((error) => {
                alert("Error logging out");
            }
        );
    },
}