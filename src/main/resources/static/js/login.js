const login = () => {
    const email = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const data = {email, password};
    callAPI.post("/api/v2/auth/authenticate-only-one-device", data).then((response) => {
        if (response["access_token"] !== undefined) {
            localStorage.setItem("token", response["access_token"]);
            localStorage.setItem("user", JSON.stringify(response["user"]));
            window.location.href = response["redirect"];
        } else {
            alert(response["message"] || "Đăng nhập thất bại");
        }
    });
}