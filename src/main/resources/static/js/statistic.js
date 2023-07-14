const from_ = document.getElementById("from");
const to_ = document.getElementById("to");


// Thống kê khách hàng
// Path: src\main\resources\static\js\statistic.js
// Customer Info
const customerInfo = {}

const getStatisticalCustomer = (from, to) => {
    const pageNumber = document.getElementById("statistic-customer-table-current-page").value;
    const pageSize = document.getElementById("statistic-customer-table-page-size").value;
    const pageInfo = {pageNumber, pageSize};
    const data = {from, to, pageInfo};
    callAPI.post(statisticURL + `/customers`, data).then(response => {
        updatePageInfo("customer", response["pageResponseInfo"]);
        loadCustomerToList(response["customers"]);
    });
}


const loadCustomerToList = (response) => {
    const customerList = document.getElementById("statistic-customer-table-body");
    customerList.innerHTML = "";
    let totalOrder = 0;
    let totalAmount = 0;
    response.forEach(customer => {
        const row = document.createElement("tr");
        row.innerHTML = `
                <td>${customer["customerNumber"]}</td>
                <td>${customer["customerName"]}</td>
                <td>${customer["totalOrder"]}</td>
                <td>${customer["totalAmount"]}</td>
            `;
        customerList.appendChild(row);
        totalOrder += customer["totalOrder"];
        totalAmount += customer["totalAmount"];
    });
    // statistic-customer-table-body-total-money
    document.getElementById("statistic-customer-table-body-total-money").innerText = totalAmount;
    // statistic-customer-table-body-total-order
    document.getElementById("statistic-customer-table-body-total-order").innerText = totalOrder;
}

// Thống kê sản phẩm
// Path: src\main\resources\static\js\statistic.js

// Product Info
const productInfo = {
    currentPage: document.getElementById("statistic-product-table-current-page"),
    totalPage: document.getElementById("statistic-product-table-total-page"),
    totalItem: document.getElementById("statistic-product-table-total-item"),
    pageSize: document.getElementById("statistic-product-table-page-size"),
    previousPage: document.getElementById("statistic-product-table-previous-page"),
    nextPage: document.getElementById("statistic-product-table-next-page"),
    totalQuantity: document.getElementById("statistic-product-table-body-total-quantity"),
    totalAmount: document.getElementById("statistic-product-table-body-total-amount"),
    productList: document.getElementById("statistic-product-table-body")
}

const getStatisticalProduct = (from, to) => {
    const pageNumber = productInfo.currentPage.value;
    const pageSize = productInfo.pageSize.value;
    const pageInfo = {pageNumber, pageSize};
    const data = {from, to, pageInfo};
    callAPI.post(statisticURL + `/products`, data).then(response => {
        updatePageInfo("product", response["pageResponseInfo"]);
        loadProductToList(response["products"]);
    });
}

const loadProductToList = (response) => {
    const productList = productInfo.productList;
    productList.innerHTML = "";
    let totalQuantity = 0;
    let totalAmount = 0;
    response.forEach(product => {
        const row = document.createElement("tr");
        row.innerHTML = `
                <td>${product["productCode"]}</td>
                <td>${product["productName"]}</td>
                <td>${product["totalSoldQuantity"]}</td>
                <td>${product["totalAmount"]}</td>
            `;
        productList.appendChild(row);
        totalQuantity += product["totalSoldQuantity"];
        totalAmount += product["totalAmount"];
    });
    // statistic-product-table-body-total-quantity
    productInfo.totalQuantity.innerText = totalQuantity;
    // statistic-product-table-body-total-amount
    productInfo.totalAmount.innerText = totalAmount;
}

// Thống kê đơn hàng
// Path: src\main\resources\static\js\statistic.js

// Order Info
const orderInfo = {
    currentPage: document.getElementById("statistic-order-table-current-page"),
    totalPage: document.getElementById("statistic-order-table-total-page"),
    totalItem: document.getElementById("statistic-order-table-total-item"),
    pageSize: document.getElementById("statistic-order-table-page-size"),
    previousPage: document.getElementById("statistic-order-table-previous-page"),
    nextPage: document.getElementById("statistic-order-table-next-page"),
    orderList: document.getElementById("statistic-order-table-body"),
}

const statusOrder = {
    "In Process": {
        count: document.getElementById("status-order-table-body-in-process-quantity"),
        amount: document.getElementById("status-order-table-body-in-process-amount"),
    },
    "Cancelled": {
        count: document.getElementById("status-order-table-body-cancelled-quantity"),
        amount: document.getElementById("status-order-table-body-cancelled-amount"),
    },
    "On Hold": {
        count: document.getElementById("status-order-table-body-on-hold-quantity"),
        amount: document.getElementById("status-order-table-body-on-hold-amount"),
    },
    "Resolved": {
        count: document.getElementById("status-order-table-body-resolved-quantity"),
        amount: document.getElementById("status-order-table-body-resolved-amount"),
    },
    "Disputed": {
        count: document.getElementById("status-order-table-body-disputed-quantity"),
        amount: document.getElementById("status-order-table-body-disputed-amount"),
    },
    "Shipped": {
        count: document.getElementById("status-order-table-body-shipped-quantity"),
        amount: document.getElementById("status-order-table-body-shipped-amount"),
    },
    "total": {
        count: document.getElementById("status-order-table-foot-total-quantity"),
        amount: document.getElementById("status-order-table-foot-total-amount"),
    }
}

const getStatisticalOrder = (from, to) => {
    const pageNumber = orderInfo.currentPage.value;
    const pageSize = orderInfo.pageSize.value;
    const pageInfo = {pageNumber, pageSize};
    const data = {from, to, pageInfo};
    callAPI.post(statisticURL + `/orders`, data).then(response => {
        updatePageInfo("order", response["pageResponseInfo"]);
        loadOrderToList(response["orders"]);
    });
    callAPI.post(statisticURL + `/orders/status`, data).then(response => {
        loadOrderStatusToList(response);
    });
}

const loadOrderToList = (response) => {
    const orderList = orderInfo.orderList;
    orderList.innerHTML = "";
    response.forEach(order => {
        const row = document.createElement("tr");
        row.innerHTML = `
                <td>${order["orderNumber"]}</td>
                <td>${order["orderDate"]}</td>
                <td>${order["shippedDate"]}</td>
                <td>${order["status"]}</td>
                <td>${order["customerNumber"]}</td>
                <td>${order["comment"]}</td>
            `;
        const btnChangeStatus = `<button class="btn btn-primary btn-sm" onClick="changeStatusOrder('${order["orderNumber"]}')">Change Status</button>`;
        const action = `
                <!-- Action -->
                <td>
                    <!-- change status -->
                    ${order["status"] !== 'Shipped' ? btnChangeStatus : ''}
                    <!-- view detail -->
                    <button class="btn btn-primary btn-sm" onclick="viewOrderDetail(${order["orderNumber"]})">View Detail</button>
                </td>
        `;
        row.innerHTML += action;
        orderList.appendChild(row);
    });
}
// change status order
// open modal change status order
const changeStatusOrder = (orderNumber) => {
    $("#change-status-order-number").val(orderNumber);
    $("#change-status-order-modal")['modal']("show");
}
// save change status order
const saveChangeStatusOrder = () => {
    const orderNumber = parseInt($("#change-status-order-number").val());
    const status = $("#change-status-order-status").val();
    const data = {orderNumber, status};
    callAPI.post(orderURL + `/change-status`, data).then(response => {
        if (response['message'] === 'success') {
            alert("Change status order successfully!");
            $("#change-status-order-modal")['modal']("hide");
            getStatisticalOrder(from_.value, to_.value);
        } else {
            alert("Change status order failed!");
        }
    });
}

// view order detail
// order details modal
const viewOrderDetail = async (orderNumber) => {
    $("#order-detail-modal")['modal']("show");
    const data = await callAPI.get(orderURL + `/${orderNumber}/orderDetail`);
    loadOrderDetail(orderNumber, data);
}

const loadOrderDetail = (orderNumber, response) => {
    $("#order-detail-order-number").val(orderNumber);
    const orderDetailList = $("#order-detail-table-body");
    orderDetailList.empty();
    response.forEach(orderDetail => {
        const row = document.createElement("tr");
        row.innerHTML = `
                <td>${orderDetail["productCode"]}</td>
                <td>${orderDetail["quantityOrdered"]}</td>
                <td>${orderDetail["priceEach"]}</td>
            `;
        orderDetailList.append(row);
    });
}

// thống kê trang thais đơn hàng
const loadOrderStatusToList = (response) => {
    let totalCount = 0;
    let totalAmount = 0;
    response.forEach(status => {
        statusOrder[status["status"]]["count"].innerText = status["count"];
        statusOrder[status["status"]]["amount"].innerText = status["amount"];
        totalCount += status["count"];
        totalAmount += status["amount"];
    });
    statusOrder["total"]["count"].innerText = totalCount;
    statusOrder["total"]["amount"].innerText = totalAmount;
}


const updatePageInfo = (id, pageResponseInfo) => {
    switch (id) {
        case "customer": {
            document.getElementById("statistic-customer-table-current-page").value = pageResponseInfo["currentPage"];
            document.getElementById("statistic-customer-table-current-page").innerText = pageResponseInfo["currentPage"] === null ? 1 : pageResponseInfo["currentPage"];
            document.getElementById("statistic-customer-table-total-page").value = pageResponseInfo["totalPages"];
            document.getElementById("statistic-customer-table-total-page").innerText = pageResponseInfo["totalPages"];
            break;
        }
        case "product": {
            productInfo.currentPage.value = pageResponseInfo["currentPage"];
            productInfo.currentPage.innerText = pageResponseInfo["currentPage"] === null ? 1 : pageResponseInfo["currentPage"];
            productInfo.totalPage.value = pageResponseInfo["totalPages"];
            productInfo.totalPage.innerText = pageResponseInfo["totalPages"];
            // productInfo.totalItem.value = pageResponseInfo["totalItems"];
            // productInfo.totalItem.innerText = pageResponseInfo["totalItems"];
            break;
        }
        case "order": {
            orderInfo.currentPage.value = pageResponseInfo["currentPage"];
            orderInfo.currentPage.innerText = pageResponseInfo["currentPage"] === null ? 1 : pageResponseInfo["currentPage"];
            orderInfo.totalPage.value = pageResponseInfo["totalPages"];
            orderInfo.totalPage.innerText = pageResponseInfo["totalPages"];
            // orderInfo.totalItem.value = pageResponseInfo["totalItems"];
            // orderInfo.totalItem.innerText = pageResponseInfo["totalItems"];
            break;
        }
    }

}

// next page
const nextPage = (id) => {
    switch (id) {
        case "customer": {
            const currentPage = document.getElementById("statistic-customer-table-current-page");
            const totalPage = document.getElementById("statistic-customer-table-total-page");
            if (+currentPage.value < +totalPage.value) {
                currentPage.value++;
                getStatisticalCustomer(from_.value, to_.value);
            }
            break;
        }
        case "product": {
            const currentPage = productInfo.currentPage;
            const totalPage = productInfo.totalPage;
            if (+currentPage.value < +totalPage.value) {
                currentPage.value++;
                getStatisticalProduct(from_.value, to_.value);
            }
            break;
        }
        case "order": {
            const currentPage = orderInfo.currentPage;
            const totalPage = orderInfo.totalPage;
            if (+currentPage.value < +totalPage.value) {
                currentPage.value++;
                getStatisticalOrder(from_.value, to_.value);
            }
            break;
        }

    }
}
// previous page
const previousPage = (id) => {
    switch (id) {
        case "customer": {
            const currentPage = document.getElementById("statistic-customer-table-current-page");
            if (+currentPage.value > 1) {
                currentPage.value--;
                getStatisticalCustomer(from_.value, to_.value);
            }
            break;
        }
        case "product": {
            const currentPage = productInfo.currentPage;
            if (+currentPage.value > 1) {
                currentPage.value--;
                getStatisticalProduct(from_.value, to_.value);
            }
            break;
        }
        case "order": {
            const currentPage = orderInfo.currentPage;
            if (+currentPage.value > 1) {
                currentPage.value--;
                getStatisticalOrder(from_.value, to_.value);
            }
        }

    }
}

// clear all values
const clearAll = () => {
    for (let item in statusOrder) {
        statusOrder[item]["count"].innerText = 0;
        statusOrder[item]["amount"].innerText = 0;
    }
}

const getStatistical = () => {
    const from = from_.value;
    const to = to_.value;
    clearAll();

    getStatisticalCustomer(from, to);
    getStatisticalProduct(from, to);
    getStatisticalOrder(from, to);
}

(() => {
// init value for date input
    from_.valueAsDate = new Date(new Date().setDate(1));
    to_.valueAsDate = new Date();
    getStatistical();
})();

