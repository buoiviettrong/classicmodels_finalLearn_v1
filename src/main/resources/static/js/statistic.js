(() => {
// init value for date input
    const today = new Date();
    const dd = today.getDate();
    const mm = today.getMonth() + 1; //January is 0!
    const yyyy = today.getFullYear();
    const from = document.getElementById("from");
    const to = document.getElementById("to");
    from.value = `${yyyy}-${mm}-${dd}`;
    to.value = `${yyyy}-${mm}-${dd}`;
})();
const from_ = document.getElementById("from");
const to_ = document.getElementById("to");
// Thống kê
const getStatistical = () => {
    const from = from_.value;
    const to = to_.value;
    getStatisticalCustomer(from, to);
    getStatisticalProduct(from, to);
}

// Thống kê khách hàng
// Path: src\main\resources\static\js\statistic.js
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
    totalOrder: document.getElementById("statistic-order-table-body-total-order"),
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
}

const loadOrderToList = (response) => {
    const orderList = orderInfo.orderList;
    orderList.innerHTML = "";
    let totalOrder = 0;
    response.forEach(order => {
        const row = document.createElement("tr");
        row.innerHTML = `
                <td>${order["orderNumber"]}</td>
                <td>${order["orderDate"]}</td>
                <td>${order["customerName"]}</td>
                <td>${order["totalQuantity"]}</td>
                <td>${order["totalAmount"]}</td>
            `;
        orderList.appendChild(row);
        totalOrder += order["totalAmount"];
    });
    // statistic-order-table-body-total-order
    orderInfo.totalOrder.innerText = totalOrder;
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