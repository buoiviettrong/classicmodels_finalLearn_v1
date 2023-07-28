class OrderClass {
    constructor() {
        this.$root = $('#root');
        this.$tableBodyData = {orders: [], pageResponseInfo: {}};
    }

    init() {
        this.$root.empty();
        this.setFilter();
        this.getData().then();
    }

    setFilter() {
        const filter = `
        <div class="row" id="filter">
                <div class="col">
                    <div class="row">
                        <div class="col-4">
                            <div class="row input-group">
                                <label class="input-group-text" for="filter-date">From Date</label>
                                <input type="date" class="form-control" id="filter-from-date">
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="row input-group">
                                <label class="input-group-text" for="filter-date">To Date</label>
                                <input type="date" class="form-control" id="filter-to-date">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-4 input-group">
<!--                    Shipped-->
<!--                    In Process-->
<!--                    Resolved-->
<!--                    Cancelled-->
<!--                    On Hold-->
<!--                    Disputed-->
                    <label class="input-group-text" for="filter-status">Status</label>
                    <select class="form-control" id="filter-status">
                        <option value="All" selected>All</option>
                        <option value="Shipped">Shipped</option>
                        <option value="In Process">In Process</option>
                        <option value="Resolved">Resolved</option>
                        <option value="Cancelled">Cancelled</option>
                        <option value="On Hold">On Hold</option>
                        <option value="Disputed">Disputed</option>
                    </select>
                </div>
                <div class="col-2 input-group">
                    <label class="input-group-text" for="filter-payment-status">Payment Status</label>
                    <select class="form-control" id="filter-payment-status">
                        <option value="All" selected>All</option>
                        <option value="UNPAID">UNPAID</option>
                        <option value="PAID">PAID</option>
                    </select>
                </div>
                <div class="col-1"><button class="btn btn-primary" onclick="orderClass.getData(1)">Search</button></div>
        </div>
        `
        this.$root.append(filter);
        Utility.setDefaultDate();
    }

    setBody() {
        $('#table').remove();
        this.setPagination();
        const tableHead = `
        <thead>
            <tr>
                <th>Mã Hóa Đơn</th>
                <th>Ngày Đặt Hàng</th>
                <th>Ngày Giao Hàng</th>
                <th>Trạng Thái</th>
                <th>Mã Khách Hàng</th>
                <th>Ghi Chú</th>
                <th>Tổng Tiền</th>
                <th>Trạng Thái Trả Tiền</th>
                <th>Ngày Trả Tiền</th>
                <th>Hành Động</th>
            </tr>
        </thead>
        `
        const tableBody = `
        <tbody>
            ${this.setTableBody()}
        </tbody>
        `
        const tableFoot = ``;
        this.$root.append(`<div class="row" id="table"><table class="table table-striped">${tableHead}${tableBody}${tableFoot}</table></div>`);
    }

    setPagination() {
        $('#pagination').remove();
        const pagination = `
        <div class="row" id="pagination">
            <div class="col-3 btn-group">
                <button class="btn btn-primary" onclick="orderClass.previousPage()">Prev</button>
                <button class="btn btn-primary" onclick="orderClass.nextPage()">Next</button>
            </div>
            <div class="col-3 btn-group">
                <input id="filter-page-number" disabled>
                <input id="filter-total-page" disabled>
            </div>
        </div>
        `
        this.$root.append(pagination);
    }

    setTableBody() {
        let rows = '';
        console.log(this.$tableBodyData);
        if (this.$tableBodyData === undefined || this.$tableBodyData['orders'] === undefined) return rows;
        this.$tableBodyData['orders'].forEach(row => {
            rows += `
            <tr>
                <td>${row['orderNumber']}</td>
                <td>${Utility.dateFormat(row.orderDate)}</td>
                <td>${row['shippedDate'] != null ? Utility.dateFormat(row['shippedDate']) : 'Đang Chờ'}</td>
                <td>${row['status']}</td>
                <td>${row['customersNumber']}</td>
                <td><textarea disabled>${row['comments']}</textarea></td>
                <td>${row['totalAmount']}</td>
                <td style="color: ${'UNPAID' === row['paymentStatus'] ? 'red' : 'green'}">${row['paymentStatus']}</td>
                <td>${row['paymentDate'] != null ? Utility.dateFormat(row['paymentDate']) : 'Chưa Trả'}</td>
                <td>
                    <div class="form-group">
                        ${"Shipped" !== row['status'] ? `<button class="btn btn-primary" onclick="OrderClass.getChangeStatus('${row['orderNumber']}')">Change Status</button>` : ""}
                        <button class="btn btn-primary" onclick="OrderClass.getDetail('${row['orderNumber']}')">Detail</button>
                    </div>
                </td>
            </tr>
            `
        })
        return rows;
    }

    async getData(pageNumber) {
        const request = {
            status: $('#filter-status').val() || 'All',
            paymentStatus: $('#filter-payment-status').val() || 'All',
            fromDate: $('#filter-from-date').val() || Utility.dateFormat(new Date()),
            toDate: $('#filter-to-date').val() || Utility.dateFormat(new Date()),
            pageNumber: pageNumber || $('#filter-page-number').val() || 1,
            pageSize: $('#filter-page-size').val() || 10,
        }
        console.log(request);
        const url = orderURL + `/admin?${$.param(request)}`
        console.log(url);
        this.$tableBodyData = await callAPI.get(url);
        this.setBody();
        this.setPageInfo();
    }

    setPageInfo() {
        const currentPage = this.$tableBodyData['pageResponseInfo']['currentPage'];
        const totalPages = this.$tableBodyData['pageResponseInfo']['totalPages'];
        const pageSize = this.$tableBodyData['pageResponseInfo']['pageSize'];
        const totalElements = this.$tableBodyData['pageResponseInfo']['totalElements'];
        $('#filter-page-number').val(currentPage);
        $('#filter-total-page').val(totalPages);
        $('#totalElement').remove();
        $('#pagination').append(`<div class="col-3 btn-group"><input disabled value="${totalElements}" id="totalElement"></div>`);
    }

    previousPage() {
        const currentPage = this.$tableBodyData['pageResponseInfo']['currentPage'];
        if (currentPage === 1) return;
        $('#filter-page-number').val(currentPage - 1);
        this.getData().then();
    }

    nextPage() {
        const currentPage = this.$tableBodyData['pageResponseInfo']['currentPage'];
        const totalPages = this.$tableBodyData['pageResponseInfo']['totalPages'];
        if (currentPage >= totalPages) return;
        $('#filter-page-number').val(currentPage + 1);
        this.getData().then();
    }

    static async getDetail(orderNumber) {
        $("#order-detail-modal")['modal']("show");
        const data = await callAPI.get(orderURL + `/${orderNumber}/orderDetail`);
        $("#order-detail-order-number").val(orderNumber);
        const orderDetailList = $("#order-detail-table-body");
        orderDetailList.empty();
        data.forEach(orderDetail => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${orderDetail["productCode"]}</td>
                <td>${orderDetail["quantityOrdered"]}</td>
                <td>${orderDetail["priceEach"]}</td>
            `;
            orderDetailList.append(row);
        });
    }

    static getChangeStatus(orderNumber) {
        $("#change-status-order-number").val(orderNumber);
        $("#change-status-order-modal")['modal']("show");
    }

    saveChangeStatus() {
        const orderNumber = parseInt($("#change-status-order-number").val());
        const status = $("#change-status-order-status").val();
        const data = {orderNumber, status};
        callAPI.post(orderURL + `/change-status`, data).then(response => {
            if (response['message'] === 'success') {
                alert("Change status order successfully!");
                $("#change-status-order-modal")['modal']("hide");
                this.getData().then();
            } else {
                alert("Change status order failed!");
            }
        });
    }
}

class Utility {
    static getFilter() {
        return {
            fromDate: $('#filter-from-date').val(),
            toDate: $('#filter-to-date').val(),
            status: $('#filter-status').val()
        };
    }

    static dateFormat(date_) {
        const date = new Date(date_);
        const year = date.getFullYear();
        const month = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : `${date.getMonth() + 1}`;
        const day = date.getDate() < 10 ? `0${date.getDate()}` : `${date.getDate()}`;
        return `${year}-${month}-${day}`;
    }

    static setDefaultDate() {
        const date = new Date();
        const year = date.getFullYear();
        const month = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : `${date.getMonth() + 1}`;
        const day = date.getDate() - 1 < 10 ? `0${date.getDate() - 1}` : `${date.getDate() - 1}`;
        $('#filter-from-date').val(`${year}-${month}-${day}`);
        $('#filter-to-date').val(`${year}-${month}-${day}`);
    }
}

const orderClass = new OrderClass();
orderClass.init();