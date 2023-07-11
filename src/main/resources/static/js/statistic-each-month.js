const year = $('#year');
const month = $('#month');
const type = $('#type');
const currentPage = $('#current-page');
const totalPage = $('#total-page');
const pageSize = $('#page-size');
const totalRecord = $('#total-record');
const tableBody = $('#table-body');
const tableHead = $('#table-head');
const myChart = document.getElementById('myChart').getContext('2d');

const globalChart = new Chart(myChart, {
    type: 'bar',
    data: {}
});

year.on('change', function () {
    loadData();
});

month.on('change', function () {
    loadData();
})

type.on('change', function () {
    loadData();
})

const loadData = () => {
    const yearValue = year.val();
    const monthValue = month.val();
    empty();
    switch (type.val()) {
        case "order":
            orders.getOrderData(yearValue);
            break;
        case "product":
            products.getProductData(yearValue, monthValue, currentPage.val(), pageSize.val());
            break;
        case "customer":
            customers.getCustomerData(yearValue, monthValue, currentPage.val(), pageSize.val());
            break;
    }
}

const empty = function () {
    emptyTable();
    currentPage.val(1);
    totalPage.val(1);
    pageSize.val(10);
    totalRecord.val(0);
}

const emptyTable = function () {
    // destroy chart
    if (orders.chart != null) orders.chart.destroy();
    if (products.chart != null) products.chart.destroy();
    // if(customers.chart) customers.chart.destroy();
    // myChart.destroy();
    tableHead.empty();
    tableBody.empty();
}
const orders = {
    head: `
        <tr>
            <th scope="col">Tháng</th>
            <th scope="col">Số Đơn Hàng</th>
            <th scope="col">Số Đơn Hoàn Thành</th>
            <th scope="col">Số Đơn Đã Hủy</th>
            <th scope="col">Trạng Thái Khác</th>
            <th scope="col">Tiền Thu Được</th>
            <th scope="col">Tiền Hoàn Trả</th>
            <th scope="col">Tiền Khác</th>
            <th scope="col">Tổng Tiền</th>
        </tr>
    `,
    chartData: {
        labels: [],
        datasets: [{
            label: 'Tiền hàng bán được',
            data: [],
            backgroundColor: '#9cf181',
            borderWidth: 1,
            borderColor: '#777',
            hoverBorderWidth: 3,
            hoverBorderColor: '#000'
        }, {
            label: 'Tiền hàng hủy',
            data: [],
            backgroundColor: '#ed5c77',
            borderWidth: 1,
            borderColor: '#777',
            hoverBorderWidth: 3,
            hoverBorderColor: '#000'
        }
        ]
    },
    getOrderData: function (yearValue) {
        const url = statisticURL + "/order-each-month" + "?year=" + yearValue;
        callAPI.get(url).then(function (response) {
            orders.loadOrderEachMonthToTable(response);
            orders.getChartDataOfOrder(response);
        }).catch(function (error) {
            console.log(error);
        });
    },
    loadOrderEachMonthToTable: function (response) {
        tableHead.append(orders.head);
        response.forEach(item => {
            let row = `
                <tr>
                    <td>${item["month"]}</td>
                    <td>${item["totalOrder"]}</td>
                    <td>${item["successOrder"]}</td>
                    <td>${item["cancellerOrder"]}</td>
                    <td>${item["otherOrder"]}</td>
                    <td>${item["successProfit"]}</td>
                    <td>${item["cancellerProfit"]}</td>
                    <td>${item["otherProfit"]}</td>
                    <td>${item["totalProfit"]}</td>
                </tr>
            `
            tableBody.append(row);
        });
    },
    getChartDataOfOrder: function (response) {
        const chart = globalChart;
        chart.data = orders.chartData;
        const labels = [];
        const data = [];
        const data2 = [];
        response.forEach(item => {
            labels.push(item["month"]);
            data.push(item["successProfit"]);
            data2.push(item["cancellerProfit"]);
        });
        chart.data.labels = labels;
        chart.data["datasets"][0].data = data;
        chart.data["datasets"][1].data = data2;
        chart.update();
    }
}

orders.getOrderData(new Date().getFullYear());

const products = {
    head: `
    <tr>
        <th scope="col">Mã Sản Phẩm</th>
        <th scope="col">Tên Sản Phẩm</th>
        <th scope="col">Số Sản Phẩm Đã Bán</th>
        <th scope="col">Tiền Bán Được</th>
    `,
    chartData: {
        labels: [],
        datasets: [{
            label: 'Tiền hàng bán được',
            data: [],
            backgroundColor: '#9cf181',
            borderWidth: 1,
            borderColor: '#777',
            hoverBorderWidth: 3,
            hoverBorderColor: '#000'
        }, {
            label: 'Tiền hàng hủy',
            data: [],
            backgroundColor: '#ed5c77',
            borderWidth: 1,
            borderColor: '#777',
            hoverBorderWidth: 3,
            hoverBorderColor: '#000'
        }]
    },
    getProductData: function (yearValue, monthValue, pageNumber, pageSize) {
        const url = `${statisticURL}/product-each-month?year=${yearValue}&month=${monthValue}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
        callAPI.get(url).then(function (response) {
            products.updatePageInfo(response);
            products.loadProductEachMonthToTable(response);
            products.getChartDataOfProduct(response);
        });
    },
    loadProductEachMonthToTable: function (response) {
        emptyTable();
        tableHead.append(products.head);
        response["products"].forEach(item => {
            let row = `
                <tr>
                    <td>${item["productCode"]}</td>
                    <td>${item["productName"]}</td>
                    <td>${item["totalSoldQuantity"] === null ? 0 : item["totalSoldQuantity"]}</td>
                    <td>${item["totalAmount"] === null ? 0 : item["totalAmount"]}</td>
                `;
            tableBody.append(row);
        });
    },
    getChartDataOfProduct: function (response) {
    },
    updatePageInfo: function (response) {
        const pageInfo = response["pageResponseInfo"];
        currentPage.val(pageInfo["currentPage"]);
        totalPage.val(pageInfo["totalPages"]);
        pageSize.val(pageInfo["pageSize"]);
        totalRecord.val(pageInfo["totalElements"]);
    }
}

const customers = {
    head: `
        <tr>
            <th scope="col">Mã Khách Hàng</th>
            <th scope="col">Tên Khách Hàng</th>
            <th scope="col">Số Đơn Đã Mua</th>
            <th scope="col">Tiền Bán Được</th>
        </tr>
    `,
    chartData: {},
    getCustomerData: function (yearValue, monthValue, pageNumber, pageSize) {
        const url = `${statisticURL}/customer-each-month?year=${yearValue}&month=${monthValue}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
        callAPI.get(url).then(function (response) {
            customers.updatePageInfo(response);
            customers.loadCustomerEachMonthToTable(response);
            customers.getChartDataOfCustomer(response);
        });
    },
    loadCustomerEachMonthToTable: function (response) {
        emptyTable();
        tableHead.append(customers.head);
        response["customers"].forEach(item => {
            let row = `
                <tr>
                    <td>${item["customerNumber"]}</td>
                    <td>${item["customerName"]}</td>
                    <td>${item["totalOrder"] === null ? 0 : item["totalOrder"]}</td>
                    <td>${item["totalAmount"] === null ? 0 : item["totalAmount"]}</td>
                `;
            tableBody.append(row);
        });
    },
    updatePageInfo: function (response) {
        const pageInfo = response["pageResponseInfo"];
        currentPage.val(pageInfo["currentPage"]);
        totalPage.val(pageInfo["totalPages"]);
        pageSize.val(pageInfo["pageSize"]);
        totalRecord.val(pageInfo["totalElements"]);
    },
    getChartDataOfCustomer: function (response) {
    }
}

// next page
const nextPage = () => {
    if (+currentPage.val() < +totalPage.val()) {
        currentPage.val(parseInt(currentPage.val()) + 1);
        switch (type.val()) {
            case "order":
                orders.getOrderData(year.val());
                break;
            case "product":
                products.getProductData(year.val(), month.val(), currentPage.val(), pageSize.val());
                break;
            case "customer":
                customers.getCustomerData(year.val(), month.val(), currentPage.val(), pageSize.val());
                break;
        }
    }
}

// previous page
const previousPage = () => {
    if (+currentPage.val() > 1) {
        currentPage.val(parseInt(currentPage.val()) - 1);
        switch (type.val()) {
            case "order":
                orders.getOrderData(year.val());
                break;
            case "product":
                products.getProductData(year.val(), month.val(), currentPage.val(), pageSize.val());
                break;
            case "customer":
                customers.getCustomerData(year.val(), month.val(), currentPage.val(), pageSize.val());
                break;
        }
    }
}