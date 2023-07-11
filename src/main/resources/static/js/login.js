const login = () => {
    const email = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const data = {email, password};
    callAPI.post("/api/v1/auth/authenticate", data).then((response) => {
        if (response["access_token"] !== undefined) {
            localStorage.setItem("token", response["access_token"]);
            localStorage.setItem("user", JSON.stringify(response["user"]));
            window.location.href = response["redirect"];
        } else {
            alert("Login failed");
        }
    });
}