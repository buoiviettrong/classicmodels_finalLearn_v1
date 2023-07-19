const year = $('#year');
const month = $('#month');
month.hide();
const type = $('#type');
const currentPage = $('#current-page');
const totalPage = $('#total-page');
const pageSize = $('#page-size');
const totalRecord = $('#total-record');
const tableBody = $('#table-body');
const tableHead = $('#table-head');
const globalFilter = $('#filter');
const formatDay = (day) => {
    if (day == null) return 'Đang Chờ';
    day = new Date(day);
    const year = day.getFullYear();
    const month = day.getMonth() + 1;
    const date = day.getDate();
    return `${date}-${month}-${year}`;
}

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
            month.hide();
            orders.getOrderData(yearValue);
            break;
        case "product":
            month.show();
            products.getProductData(yearValue, monthValue, currentPage.val(), pageSize.val());
            break;
        case "customer": {
            month.show();
            customers.filter.init();
            customers.getCustomerData(customers.filter.getFilterValue(), yearValue, monthValue, currentPage.val(), pageSize.val());
            break;
        }
    }
}

const empty = function () {
    emptyTable();
    emptyFilter();
    currentPage.val(1);
    totalPage.val(1);
    pageSize.val(10);
    totalRecord.val(0);
}

const emptyTable = function () {
    tableHead.empty();
    tableBody.empty();
}

const emptyFilter = function () {
    globalFilter.empty();
}

const modals = {
    customerOrderDetails: {
        content: `
        <div class="modal fade bd-example-modal-lg" id="customerOrderDetails" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Customer Order Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="row m-2" id="tableInfo">
                  <div class="col-6 font-weight-bold" id="customerNumber"></div>
                  <div class="col-3 font-weight-bold" id="year"></div>
                  <div class="col-3 font-weight-bold" id="month"></div>
              </div>
              <div class="row m-2 font-weight-bold" id="totalStatus"></div>
              <div class="row m-2" id="statusInfo">
                  <div class="col-2" id="Shipped"></div>
                  <div class="col-2" id="Cancelled"></div>
                  <div class="col-2" id="Resolved"></div>
                  <div class="col-2" id="Disputed"></div>
                  <div class="col-2" id="InProcess"></div>
                  <div class="col-2" id="OnHold"></div>
              </div>  
              <table class="modal-body table table-bordered table-hover table-striped">
                <thead>
                    <tr>
                        <th scope="col">Mã Đơn Hàng</th>
                        <th scope="col">Ngày Đặt Hàng</th>
                        <th scope="col">Ngày Giao Hàng</th>
                        <th scope="col">Trạng Thái</th>
                        <th scope="col">Tổng Tiền</th>
                        <th scope="col">Ghi Chú</th>
                    </tr>
                </thead>
                <tbody class="body-content"></tbody>
              </table>
              <table class="modal-body table table-bordered table-hover table" id="productDetails">
                    <thead class="head-content-product"></thead>
                    <tbody class="body-content-product"></tbody>
                </table>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div>`,
        show: function (customerId, yearValue, monthValue) {
            const url = `${orderURL}/customer-order-details?customerNumber=${customerId}&year=${yearValue}&month=${monthValue}`;
            callAPI.get(url).then(function (response) {
                modals.customerOrderDetails.loadOrderDetails(customerId, response);
            }).catch(function (error) {
                console.log(error);
            });
        },
        loadOrderDetails: (customerNumber, response) => {
            const baseModal = $('#baseModal');
            baseModal.empty().append(modals.customerOrderDetails.content);
            const modal = $('#customerOrderDetails');
            modals.customerOrderDetails.updateTableInfo(customerNumber, year.val(), month.val());

            const status = {
                'total': 0,
                'Shipped': 0,
                'InProcess': 0,
                'Cancelled': 0,
                'Resolved': 0,
                'Disputed': 0,
                'OnHold': 0,
            };

            const bodyContent = modal.find('.body-content');
            response.forEach(item => {
                let row = `
                    <tr onclick="modals.customerOrderDetails.detailsOfProduct('${item["orderNumber"]}')">
                        <td>${item["orderNumber"]}</td>
                        <td>${formatDay(item["orderDate"])}</td>
                        <td>${formatDay(item["shippedDate"])}</td>
                        <td>${item["status"]}</td>
                        <td>${item["totalPrice"]}</td>
                        <td><textarea readonly>${item['comments']}</textarea></td>
                        
                    </tr>
                `
                status[item['status'].replaceAll(' ', '')]++;
                status['total']++;
                bodyContent.append(row);
            });
            modals.customerOrderDetails.updateStatusInfo(status);
            modal['modal']('show');
        },
        updateTableInfo: function (customerId, yearValue, monthValue) {
            const tableInfo = $('#tableInfo');
            tableInfo.find('#customerNumber').text(`Mã Khách Hàng: ${customerId}`);
            tableInfo.find('#year').text(`Năm: ${yearValue}`);
            tableInfo.find('#month').text(`Tháng: ${monthValue}`);
        },
        updateStatusInfo: function (response) {
            const statusInfo = $('#statusInfo');
            $('#totalStatus').text(`Tổng Đơn Hàng: ${response['total']}`);

            for (let key in response) {
                if (key !== 'total') {
                    statusInfo.find(`#${key}`).text(`${key}: ${response[key]}`);
                }
            }
        },
        detailsOfProduct: async (orderNumber) => {
            const url = `${orderURL}/${orderNumber}/orderDetail`;
            const data = await callAPI.get(url);
            modals.customerOrderDetails.loadProductDetails(data);
        },
        loadProductDetails: function (response) {
            const modal = $('#productDetails');
            const headContent = modal.find('.head-content-product');
            const bodyContent = modal.find('.body-content-product');
            bodyContent.empty();
            headContent.empty().append(`
                <tr>
                    <th scope="col">Mã Sản Phẩm</th>
                    <th scope="col">Tên Sản Phẩm</th>
                    <th scope="col">Số Lượng</th>
                    <th scope="col">Giá</th>
                </tr
            `);
            response.forEach(item => {
                let row = `
                    <tr>
                        <td>${item["productCode"]}</td>
                        <td>${item["productName"]}</td>
                        <td>${item["quantityOrdered"]}</td>
                        <td>${item["priceEach"]}</td>
                    </tr>
                `;
                bodyContent.append(row);
            });
        }
    },
    orderDetails: {
        content: `
           <div class="modal fade bd-example-modal-lg" id="orderDetails" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Order Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="row m-2" id="tableInfo">
                <div class="col-3 font-weight-bold d-flex">
                    <div class="font-weight-bold">Năm: </div>
                    <div class="font-weight-bold" id="year_"></div>
                </div>
                <div class="col-3 font-weight-bold d-flex">
                    <div class="font-weight-bold">Tháng: </div>
                    <div class="col-3 font-weight-bold" id="month_"></div>   
                </div>
              </div>
              <div class="row m-2 font-weight-bold d-flex">
                <div class="font-weight-bold">Tổng Đơn Hàng: </div>
                <div class="font-weight-bold" id="totalStatus">0</div>
              </div>
              <div class="row m-2" id="statusInfo">
                  <div class="col-2 d-flex">
                        <div class="font-weight-bold">Shipped: </div>
                        <div class="font-weight-bold" id="Shipped">0</div>
                  </div>
                  <div class="col-2 d-flex">
                      <div class="font-weight-bold">Cancelled: </div>
                      <div class="font-weight-bold" id="Cancelled">0</div>
                  </div>
                  <div class="col-2 d-flex">
                        <div class="font-weight-bold">Resolved: </div>
                        <div class="font-weight-bold" id="Resolved">0</div>
                  </div>
                  <div class="col-2 d-flex">
                        <div class="font-weight-bold">Disputed: </div>
                        <div class="font-weight-bold" id="Disputed">0</div>
                  </div>
                  <div class="col-2 d-flex">
                        <div class="font-weight-bold">InProcess: </div>
                        <div class="font-weight-bold" id="InProcess">0</div>
                  </div>
                  <div class="col-2 d-flex">
                        <div class="font-weight-bold">OnHold: </div>
                        <div class="font-weight-bold" id="OnHold">0</div>
                  </div>
              </div>  
              <div class="row m-2 d-flex" id="actions">
                <div class="col-2">
                    <div class="font-weight-bold">Tổng Trang: </div>
                    <div class="font-weight-bold" id="totalPage">0</div>
                </div>
              </div>
              <table class="modal-body table table-bordered table-hover table-striped">
                <thead>
                    <tr>
                        <th scope="col">Mã Đơn Hàng</th>
                        <th scope="col">Mã Khách Hàng</th>
                        <th scope="col">Tên Khách Hàng</th>
                        <th scope="col">Ngày Đặt Hàng</th>
                        <th scope="col">Ngày Giao Hàng</th>
                        <th scope="col">Trạng Thái</th>
                        <th scope="col">Tổng Tiền</th>
                        <th scope="col">Ghi Chú</th>
                    </tr>
                </thead>
                <tbody class="body-content"></tbody>
              </table>
              <div id="productDetails"></div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div> 
        `,
        init: function () {
            const baseModal = $('#baseModal');
            baseModal.empty().append(modals.orderDetails.content);
        },
        getData: function (year, month, pageNumber, pageSize, status) {
            status = status || 'All';
            const url = `${orderURL}/order-details?year=${year}&month=${month}&pageNumber=${pageNumber}&pageSize=${pageSize}&status=${status}`;
            callAPI.get(url).then(function (response) {
                modals.orderDetails.loadOrderDetails(year, month, response);
            });
        },
        show: function (year, month) {
            modals.orderDetails.init();
            modals.orderDetails.getData(year, month, 1, 10);
        },
        loadOrderDetails: function (year, month, response) {
            const modal = $('#orderDetails');
            const bodyContent = modal.find('.body-content');
            bodyContent.empty();
            if (response['orders'].length === 0) {
                bodyContent.append(`
                    <tr>
                        <td colspan="8" class="text-center">Không có dữ liệu</td>
                    </tr>
                `);
                modal['modal']('show');
                return;
            }
            response['orders'].forEach(item => {
                let row = `
                    <tr onclick="modals.orderDetails.loadProductDetails('${item["orderNumber"]}')">
                        <td>${item["orderNumber"]}</td>
                        <td>${item['customerNumber']}</td>
                        <td>${item['customerName']}</td>
                        <td>${formatDay(item["orderDate"])}</td>
                        <td>${formatDay(item["shippedDate"])}</td>
                        <td>${item["status"]}</td>
                        <td>${item["totalPrice"]}</td>
                        <td><textarea readonly>${item['comments'] == null ? "" : item['comments']}</textarea></td>
                        
                    </tr>
                `
                bodyContent.append(row);
            });
            const __ = modals.orderDetails;
            __.updateStatusInfo(response['status']);
            __.updateTableInfo(year, month);
            __.updateActionButtons(response['pageResponseInfo']['currentPage'], response['pageResponseInfo']['totalPages']);
            __.updateStatusFilter();
            __.updateTotalOrders(response['pageResponseInfo']['totalElements'], response['pageResponseInfo']['totalPages']);
            modal['modal']('show');
        },
        loadProductDetails: async function (orderNumber) {
            const modal = $('#productDetails');
            modal.empty();
            modal.append(`<div class="font-weight-bold">Chi Tiết Sản Phẩm</div>`)
            modal.append(`<div class="font-weight-bold">Mã Đơn Hàng: ${orderNumber}</div>`)
            modal.append(
                `<table class="modal-body table table-bordered table-hover table">
                    <thead class="head-content-product"></thead>
                    <tbody class="body-content-product"></tbody>
                </table>
                `
            )
            const headContent = modal.find('.head-content-product');
            const bodyContent = modal.find('.body-content-product');

            headContent.append(`
                <tr>
                    <th scope="col">Mã Sản Phẩm</th>
                    <th scope="col">Tên Sản Phẩm</th>
                    <th scope="col">Số Lượng</th>
                    <th scope="col">Đơn Giá</th>
                </tr>
            `)
            const url = `${orderURL}/${orderNumber}/orderDetail`;
            const data = await callAPI.get(url);

            data.forEach(item => {
                let row = `
                    <tr>
                        <td>${item['productCode']}</td>
                        <td>${item['productName']}</td>
                        <td>${item['quantityOrdered']}</td>
                        <td>${item['priceEach']}</td>
                    </tr>
                `
                bodyContent.append(row);
            })
        },
        updateTableInfo: function (yearValue, monthValue) {
            const tableInfo = $('#tableInfo');
            tableInfo.find('#year_').text(yearValue);
            tableInfo.find('#month_').text(monthValue);
        },
        updateTotalOrders: function (totalElements, totalPage) {
            $('#totalStatus').text(totalElements);
            $('#totalPage').text(totalPage);
        },
        updateActionButtons: function (currentPage, totalPages) {
            // add action buttons (prev, next) and status filter
            const actionsBtn = `
                <div class="col-6 btn-group">
                    <button 
                        type="button" 
                        class="btn btn-primary" 
                        onclick="modals.orderDetails.previousPage(${currentPage})"
                    >
                        Prev
                    </button>
                    <button 
                        type="button" 
                        class="btn btn-primary" 
                        onclick="modals.orderDetails.nextPage(${currentPage}, ${totalPages})"
                    >
                        Next
                    </button>
                </div>
            `
            $('#actions').empty().append(actionsBtn);
        },
        updateStatusFilter: function () {
            const statusFilter = `
                <div class="col-3 d-flex">
                    <label for="status">Status</label>
                    <select class="form-control" id="status" onchange="modals.orderDetails.statusChanged()">
                        <option value="All" selected>All</option>
                        <option value="Cancelled">Cancelled</option>
                        <option value="Resolved">Resolved</option>
                        <option value="Disputed">Disputed</option>
                        <option value="In Process">In Process</option>
                        <option value="On Hold">On Hold</option>
                        <option value="Shipped">Shipped</option>
                    </select>
                </div>
            `
            $('#actions').append(statusFilter);
        },
        statusChanged: () => {
            const status = $('#status').val();
            const year = parseInt($('#year_').text());
            const month = parseInt($('#month_').text());
            modals.orderDetails.getData(year, month, 1, 10, status);
        },
        updateStatusInfo: function (response) {
            const statusInfo = $('#statusInfo');
            for (let key in response) {
                if (key !== 'total') {
                    statusInfo.find(`#${key.replaceAll(' ', '')}`).text(response[key]);
                }
            }
        },
        nextPage: function (page, totalPages) {
            if (page === totalPages) {
                return;
            }
            const year = parseInt($('#year_').text());
            const month = parseInt($('#month_').text());
            modals.orderDetails.getData(year, month, page + 1, 10);
        },
        previousPage: function (page) {
            if (page === 1) {
                return;
            }
            const year = parseInt($('#year_').text());
            const month = parseInt($('#month_').text());
            modals.orderDetails.getData(year, month, page - 1, 10);
        },
    }
}

const orders = {
    head: `
        <tr>
            <th scope="col">Tháng</th>
            <th scope="col">Số Đơn Hàng</th>
<!--            <th scope="col">Số Đơn Hoàn Thành</th>-->
<!--            <th scope="col">Số Đơn Đã Hủy</th>-->
<!--            <th scope="col">Trạng Thái Khác</th>-->
<!--            <th scope="col">Tiền Thu Được</th>-->
<!--            <th scope="col">Tiền Hoàn Trả</th>-->
<!--            <th scope="col">Tiền Khác</th>-->
            <th scope="col">Tổng Tiền</th>
            <th scope="col">Tổng Tiền Thu Thật Tế</th>
        </tr>
    `,
    getOrderData: function (yearValue) {
        const url = statisticURL + "/order-each-month" + "?year=" + yearValue;
        callAPI.get(url).then(function (response) {
            orders.loadOrderEachMonthToTable(response);
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
                    <td onclick="orders.getOrderDetails('${item["month"]}')">${item["totalOrder"]}</td>
<!--                    <td>${item["successOrder"]}</td>-->
<!--                    <td>${item["cancellerOrder"]}</td>-->
<!--                    <td>${item["otherOrder"]}</td>-->
<!--                    <td>${item["successProfit"]}</td>-->
<!--                    <td>${item["cancellerProfit"]}</td>-->
<!--                    <td>${item["otherProfit"]}</td>-->
                    <td>${item["totalProfit"]}</td>
                    <td>${item['successProfit']}</td>
                </tr>
            `
            tableBody.append(row);
        });
    },
    getOrderDetails: function (month) {
        modals.orderDetails.show(year.val(), month);
    }
}

orders.getOrderData(new Date().getFullYear());

const products = {
    head: `
    <tr>
        <th scope="col">Mã Sản Phẩm</th>
        <th scope="col">Tên Sản Phẩm</th>
        <th scope="col">Số Sản Phẩm Đã Bán</th>
        <th scope="col">Giá Bán</th>
        <th scope="col">Tiền Bán Được</th>
        <th scope="col">Giá Nhập</th>
        <th scope="col">Lợi Nhuận</th>
    `,
    getProductData: function (yearValue, monthValue, pageNumber, pageSize) {
        const url = `${statisticURL}/product-each-month?year=${yearValue}&month=${monthValue}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
        callAPI.get(url).then(function (response) {
            products.updatePageInfo(response);
            products.loadProductEachMonthToTable(response);
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
                    <td>${item["soldPrice"] || 0}</td>
                    <td>${item["totalAmount"] === null ? 0 : item["totalAmount"]}</td>
                    <td>${item["buyPrice"] === null ? 0 : item["buyPrice"]}</td>
                    <td>${item["totalProfit"] === null ? 0 : item["totalProfit"]}</td>
                </tr>   
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
    filter: {
        content: `
            <div class="col-6 input-group">
                <label class="input-group-text" for="customerNameFilter">Filter</label>
                <input class="form-control" type="text" value="" placeholder="Customer Name" id="customerNameFilter">
                <button class="btn btn-outline-primary" type="button" id="button-addon2" onclick="customers.filter.onChange()">Search</button>
            </div>
        `,
        init: function () {
            globalFilter.html(customers.filter.content);
        },
        getFilterValue: function () {
            return $('#customerNameFilter').val();
        },
        onChange: () => {
            customers.getCustomerData(
                customers.filter.getFilterValue(),
                year.val(),
                month.val(),
                currentPage.val(),
                pageSize.val()
            );
        }
    },
    getCustomerData: function (customerName, yearValue, monthValue, pageNumber, pageSize) {
        const url = `${statisticURL}/customer-each-month?customerName=${customerName}&year=${yearValue}&month=${monthValue}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
        callAPI.get(url).then(function (response) {
            customers.updatePageInfo(response);
            customers.loadCustomerEachMonthToTable(response);
            customers.getChartDataOfCustomer(response);
        });
    },
    loadCustomerEachMonthToTable: function (response) {
        emptyTable();
        tableHead.append(customers.head);
        if (response["customers"].length === 0) {
            tableBody.append(`
                <tr>
                    <td colspan="4" class="text-center">Không có dữ liệu</td>
                </tr>
            `);
            return;
        }
        response["customers"].forEach(item => {
            let row = `
                <tr onclick="customers.event.getOrderDetail('${item['customerNumber']}')">
                    <td>${item["customerNumber"]}</td>
                    <td>${item["customerName"]}</td>
                    <td class="pe-auto">${item["totalOrder"] === null ? 0 : item["totalOrder"]}</td>
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
    },
    event: {
        getOrderDetail: function (customerId) {
            modals.customerOrderDetails.show(customerId, year.val(), month.val());
        }
    }
}

// next page
const nextPage = () => {
    if (+currentPage.val() < +totalPage.val()) {
        currentPage.val(parseInt(currentPage.val()) + 1);
        load(type.val());
    }
}

// previous page
const previousPage = () => {
    if (+currentPage.val() > 1) {
        currentPage.val(parseInt(currentPage.val()) - 1);
        load(type.val());
    }
}

const load = (val) => {
    switch (val) {
        case "order":
            orders.getOrderData(year.val());
            break;
        case "product":
            products.getProductData(year.val(), month.val(), currentPage.val(), pageSize.val());
            break;
        case "customer":
            customers.getCustomerData(customers.filter.getFilterValue(), year.val(), month.val(), currentPage.val(), pageSize.val());
            break;
    }
}