const loadCustomers = () => {
    callAPI.get(customerURL).then((response) => {
        let content = "";
        console.log(response);
        for (let i = 0; i < response.length; i++) {
            content += `
            <tr>
                <td>${response[i]["customerNumber"]}</td>
                <td>${response[i]["customerName"]}</td>
                <td>${response[i]["contactLastName"]}</td>
                <td>${response[i]["contactFirstName"]}</td>
                <td>${response[i]["phone"]}</td>
                <td>${response[i]["addressLine1"]}</td>
                <td>${response[i]["addressLine2"]}</td>
                <td>${response[i]["city"]}</td>
                <td>${response[i]["state"]}</td>
                <td>${response[i]["postalCode"]}</td>
                <td>${response[i]["country"]}</td>
                <td>${response[i]["salesRepEmployeeNumber"] == null ? null : response[i]["salesRepEmployeeNumber"]["employeeNumber"]}</td>
                <td>${response[i]["creditLimit"]}</td>
                <td>
                    <a href="#" class="btn btn-primary" onclick="getCustomer(${response[i]["customerNumber"]})">Edit</a>
                    <a href="#" class="btn btn-danger" onclick="deleteCustomer(${response[i]["customerNumber"]})">Delete</a>
                </td>
            </tr>
            `;
        }
        document.getElementById("customerList").innerHTML = content;
    })
}
loadCustomers();

const getCustomer = (id) => {

}

const deleteCustomer = (id) => {
    callAPI.delete(customerURL + "/" + id).then((response) => {
        alert("Delete customer successfully!");
    });
}