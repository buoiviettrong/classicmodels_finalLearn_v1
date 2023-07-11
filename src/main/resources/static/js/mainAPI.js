const headers = {
    "Content-Type": "application/json",
    "Accept": "application/json",
};

const checkStatus = (response) => {
    if (response.status >= 200 && response.status < 300) {
        return response;
    } else {
        const res = response.json();
        console.log(res)
        alert(res.message);
    }
}

const callAPI = {
    post: (url, data) => {
        if (localStorage.getItem("token") !== null)
            headers["Authorization"] = "Bearer " + localStorage.getItem("token");
        return fetch(host + url, {
            method: "POST",
            headers: headers,
            body: JSON.stringify(data)
        }).then((response) => checkStatus(response).json())
            .catch((error) => console.log(error));
    },
    get: (url) => {
        if (localStorage.getItem("token") !== null)
            headers["Authorization"] = "Bearer " + localStorage.getItem("token");
        return fetch(host + url, {
            method: "GET",
            headers: headers,
        }).then((response) => checkStatus(response).json())
            .catch((error) => console.log(error));
    },
    put: (url, data) => {
        if (localStorage.getItem("token") !== null)
            headers["Authorization"] = "Bearer " + localStorage.getItem("token");
        return fetch(host + url, {
            method: "PUT",
            headers: headers,
            body: JSON.stringify(data)
        }).then((response) => checkStatus(response).json())
            .catch((error) => console.log(error));
    },
    delete: (url) => {
        if (localStorage.getItem("token") !== null)
            headers["Authorization"] = "Bearer " + localStorage.getItem("token");
        return fetch(host + url, {
            method: "DELETE",
            headers: headers,
        }).then((response) => checkStatus(response).json())
            .catch((error) => console.log(error));
    }
}