class AdminSyntheticStatistic {

    constructor() {
        this.$overview = $('#overview');
        this.$detail = $('#detail');
        this.$table = $('#table');
    }

    async init() {
        StatisticEvent.clear();
        this.$overviewData = {
            overviewTotal: {
                totalInvoice: 0,
                totalMoney: 0,
                totalSoldProduct: 0,
            },
            overviewTop: {
                invoice: {
                    orderNumber: 0,
                    orderDate: Utility.formatDate(new Date()),
                    totalMoney: 0.0
                },
                product: {
                    productCode: 'productCode',
                    productName: 'productName',
                    quantity: 0
                },
                productLine: {
                    productLineCode: 'productLineCode',
                    quantity: 0
                },
                customer: {
                    customerNumber: 0,
                    customerName: 'customerName',
                    quantityInvoice: 0,
                    totalMoney: 0.0
                }
            }
        };
        this.$detailData = {
            totalProduct: 0,
            syntheticProductLine: [{productLineCode: '', totalSoldProduct: 0, totalMoney: 0.0}]
        };
        await this.getData();
    }

    async getData() {
        const url = statisticURL + "/admin-statistical";
        const request = {
            from: $('#from-date').val(),
            to: $('#to-date').val(),
            type: ''
        }
        const data = await callAPI.post(url, request);
        console.log(data)
        this.$overviewData = data.overview;
        this.$detailData = data['syntheticProduct'];
        this.setOverview();
        // this.setDetails();
        this.setTable();
    }

    setOverview() {
        this.$overview.empty();

        const overviewTemplateTotal = `
        <div class="col-4 card">
            <div class="row d-flex justify-content-between">
                <div class="col">Tổng Hóa Đơn</div>
                <div class="col">${this.$overviewData.overviewTotal.totalInvoice}</div>
            </div>
            <div class="row d-flex justify-content-between">
                <div class="col">Tổng Tiền Thu Được</div>
                <div class="col">${this.$overviewData.overviewTotal.totalMoney}</div>
            </div>
            <div class="row d-flex justify-content-between">
                <div class="col">Tổng Số Sản Phẩm Bán Được</div>
                <div class="col">${this.$overviewData.overviewTotal.totalSoldProduct}</div>
            </div>
        </div>
        `;
        const overviewTemplateTop = `
        <div class="col-8 card">
            <div class="row d-flex justify-content-between form-group">
                <div class="col input-group-text">Hóa Đơn Thu Được Cao Nhất</div>
                <div class="col form-control">
                    <div class="row form-group">
                        <div class="col input-group-text">Mã hóa đơn</div>
                        <div class="col form-control">${this.$overviewData.overviewTop.invoice.orderNumber}</div>
                    </div>
                    <div class="row form-group">
                        <div class="col input-group-text">Ngày đặt</div>
                        <div class="col form-control">${Utility.formatDate(this.$overviewData.overviewTop.invoice.orderDate)}</div>
                        
                    </div>
                    <div class="row form-group">
                        <div class="col input-group-text">Tổng tiền</div>
                        <div class="col form-control">${this.$overviewData.overviewTop.invoice.totalMoney}</div>
                    </div>  
                </div>
            </div>
            <hr>
            <div class="row d-flex justify-content-between form-group">
                <div class="col input-group-text">Sản Phẩm Bán Chạy Nhất</div>
                <div class="col form-control">
                    <div class="row form-group">
                        <div class="col input-group-text">Mã sản phẩm</div>
                        <a class="col form-control">${this.$overviewData.overviewTop.product.productCode}</a>
                    </div>
                    <div class="row form-group">
                        <div class="col input-group-text">Tên sản phẩm</div>
                        <div class="col form-control">${this.$overviewData.overviewTop.product.productName}</div>
                    </div>
                    <div class="row form-group">
                        <div class="col input-group-text">Số lượng bán</div>
                        <div class="col form-control">${this.$overviewData.overviewTop.product.quantity}</div>
                    </div>
                </div>
            </div>
            <hr>
            <div class="row d-flex justify-content-between form-group">
                <div class="col input-group-text">Dòng Sản Phẩm Đặt Mua Nhiều Nhất</div>
                <div class="col form-control">
                    <div class="row form-group">
                        <div class="col input-group-text">Mã dòng sản phẩm</div>
                        <a class="col form-control">${this.$overviewData.overviewTop.productLine.productLineCode}</a>
                    </div>
                    <div class="row form-group">
                        <div class="col input-group-text">Số lượng đặt mua</div>
                        <div class="col form-control">${this.$overviewData.overviewTop.productLine.quantity}</div>
                    </div>
                </div>
            </div>
            <hr>
            <div class="row d-flex justify-content-between form-group">
                <div class="col input-group-text">Khách Hàng Đặt Mua Nhiều Nhất</div>
                <div class="col form-control">
                    <div class="row form-group">
                        <div class="col input-group-text">Mã khách hàng</div>
                        <a class="col form-control">${this.$overviewData.overviewTop.customer.customerNumber}</a>
                    </div>
                    <div class="row form-group">
                        <div class="col input-group-text">Tên khách hàng</div>
                        <div class="col form-control">${this.$overviewData.overviewTop.customer.customerName}</div>
                    </div>
                    <div class="row form-group">
                        <div class="col input-group-text">Số lượng đặt mua</div>
                        <div class="col form-control">${this.$overviewData.overviewTop.customer.quantityInvoice}</div>
                    </div>
                    <div class="row form-group">
                        <div class="col input-group-text">Tổng tiền</div>
                        <div class="col form-control">${this.$overviewData.overviewTop.customer.totalMoney}</div>
                    </div>
                </div>
            </div>
        </div>
        `
        this.$overview.append(overviewTemplateTotal);
        this.$overview.append(overviewTemplateTop);
    };

    setTable() {
        this.$table.empty();
        const tableHeader = `
            <thead class="table-header">
                <th>Mã Dòng Sản Phẩm</th>
                <th>Số Lượng Bán Được</th>
                <th>Tổng Tiền</th>
            </thead>
        `;
        let tableRow = ``;
        this.$detailData.syntheticProductLine.forEach(item => {
            tableRow += `
                <tr class="table-row">
                    <td>${item.productLineCode}</td>
                    <td>${item.totalSoldProduct}</td>
                    <td>${item.totalMoney}</td>
                </tr>
            `
        })
        const tableBody = `<tbody class="table-body">${tableRow}</tbody>`;
        const tableTemplate = `<table class="table">${tableHeader}${tableBody}</table`;
        this.$table.append(tableTemplate);
    }

    setDetails() {
        this.$detail.empty();
        const detailTemplate = `
        <div class="chart-container" style="position: relative; height:40vh; width:80vw">
            <canvas id="myChart"></canvas>
        </div>
        `;
        this.$detail.append(detailTemplate);
        const ctx = document.getElementById('myChart').getContext('2d');

        const labels = this.$detailData.map((item) => {
            return Utility.formatDate(item.date);
        });
        const datasets = {
            label: 'Doanh thu',
            backgroundColor: 'green',
            data: this.$detailData.map((item) => {
                return item.totalMoney;
            })
        }
        Chart_.createChart(ctx, labels, datasets);
    }
}

class AdminDetailStatistics {
    constructor() {
        this.$overview = $('#overview');
        this.$table = $('#table');
        this.$otherFilter = $('#other-filter');
        this.$overviewData = {
            totalMoney: 0,
            totalSold: 0,
        };

        this.$tableData = {
            products: [{
                productCode: 'productCode',
                productName: 'productName',
                productLine: 'productLine',
                quantitySold: 0,
                totalMoney: 0.0
            }],
            totalQuantity: 0,
            totalMoney: 0.0,
            pageResponseInfo: {
                currentPage: 0,
                pageSize: 0,
                totalElements: 0,
                totalPages: 0,
                totalElementOfCurrentPage: 0
            }
        };
    }

    init() {
        console.log('admin detail statistic init');
        StatisticEvent.clear();

        this.setMoreFilter();
        this.getProductLine().then();
    }

    setMoreFilter() {
        this.$otherFilter.empty();
        console.log('admin detail statistic set more filter');
        const component = `
                <div class="col-3 form-group mr-3">
                    <div class="row">
                        <label for="product-line-select" class="input-group-text col">Dòng sản phẩm</label>
                        <select id="product-line-select" class="form-control col">
                            <option value="0">Tất cả</option>
                        </select>
                    </div>
                </div>
                <div class="col-4 form-group ml-3">
                    <div class="row">
                        <label for="product-input" class="input-group-text col">Theo tên sản phẩm</label>
                        <input type="text" class="form-control col" placeholder="productCode, productName"> 
                    </div>
                </div>
        `;
        this.$otherFilter.append(component);
    }

    async getData(pageNumber = 1, pageSize = 10) {
        console.log('admin detail statistic get data');
        const url = statisticURL + `/admin-statistical-detail?pageNumber=${pageNumber}&pageSize=${pageSize}`;
        const request = {
            from: $('#from-date').val(),
            to: $('#to-date').val(),
            typeProductLine: $('#product-line-select').val(),
            search: $('#product-input').val()
        }
        const data = await callAPI.post(url, request);
        console.log(data);

        this.$overviewData = data.overview;
        this.$tableData = data.table;
        this.setOverview();
        this.setTable();
    }

    setOverview() {
        this.$overview.empty();
        const overviewTemplate = `
            <div class="col">
                <div class="card m-3">
                    <div class="row">
                        <div class="col">Dòng Sản Phẩm</div>
                        <div class="col">${this.$overviewData.productLineCode}</div>
                    </div>
                    <div class="row">
                        <div class="col">Tổng Số Đã Bán</div>
                        <div class="col">${this.$overviewData.totalSold}</div>
                    </div>
                    <div class="row">
                        <div class="col">Tổng Tiền Thu Được</div>
                        <div class="col">${this.$overviewData.totalMoney}</div>
                    </div>
                </div>
            </div>
        `
        this.$overview.append(overviewTemplate);
    }

    setTable() {
        this.$table.empty();
        const tableHeader = `
            <thead class="table-header">
                <th>Mã Sản Phẩm</th>
                <th>Tên Sản Phẩm</th>
                <th>Dòng Sản Phẩm</th>
                <th>Số Lượng Đã Bán</th>
                <th>Tổng Tiền</th>
            </thead>
        `;
        let tableRow = ``;
        if (this.$tableData.products.length > 0)
            this.$tableData.products.forEach(item => {
                tableRow += `
                <tr class="table-row">
                    <td>${item.productCode}</td>
                    <td>${item.productName}</td>
                    <td>${item.productLine}</td>
                    <td>${item.quantitySold}</td>
                    <td>${item.totalMoney}</td>
                </tr>
            `
            });
        else
            tableRow = `<tr><th colspan="5">No Item</th>></tr>`
        const tableBody = `<tbody class="table-body">${tableRow}</tbody>`;
        const tableFoot = `
        <tfoot class="table-foot">
            <tr>
                <th colspan="3">Tổng</th>
                <th>${this.$tableData.totalQuantity}</th>
                <th>${this.$tableData.totalMoney}</th>
            </tr>
        </tfoot>`;
        const tableTemplate = `<table class="table">${tableHeader}${tableBody}${tableFoot}</table`;

        const paging = `
            <div class="row d-flex justify-content-center">
                <div class="col-2">
                    <button class="btn btn-primary" id="previous-btn" onclick="AdminDetailStatistics.previousPage(${this.$tableData.pageResponseInfo.currentPage})">Previous</button>
                </div>
                <div class="col-2">
                    <button class="btn btn-primary" id="next-btn" onclick="AdminDetailStatistics.nextPage(${this.$tableData.pageResponseInfo.currentPage})">Next</button>
                </div>
                <div class="col-2">
                    <div class="row form-group">
                        <label for="current-page" class="input-group-text col">Trang</label>
                        <input id="current-page" type="text" class="form-control col" value="${this.$tableData.pageResponseInfo.currentPage}">
                    </div>
                </div>
                <div class="col-3">
                    <div class="row form-group">
                        <label for="total-page" class="input-group-text col">Tổng Trang</label>
                        <input id="total-page" type="text" class="form-control col" value="${this.$tableData.pageResponseInfo.totalPages}" disabled>
                    </div>
                </div>
            </div>
            <br>
        `
        this.$table.append(paging);
        this.$table.append(tableTemplate);
    }

    static async nextPage(currentPage) {
        const totalPage = $('#total-page')
        if (currentPage < +totalPage.val()) {
            const adminDetailStatistics = new AdminDetailStatistics();
            await adminDetailStatistics.getData(currentPage + 1, 10);
        }
    }

    static async previousPage(currentPage) {
        if (currentPage > 1) {
            const adminDetailStatistics = new AdminDetailStatistics();
            await adminDetailStatistics.getData(currentPage - 1, 10);
        }
    }

    async getProductLine() {
        const selectedProductLine = $('#product-line-select');
        selectedProductLine.empty();
        selectedProductLine.append(`<option value="0">Tất cả</option>`);
        const url = productLineURL + '/select';
        const data = await callAPI.get(url);
        console.log(data);
        data.forEach(item => {
            const option = `<option value="${item}">${item}</option>`;
            selectedProductLine.append(option);
        });
    }

}

class Chart_ {
    static createChart(ctx, labels, datasets) {
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                colors: 'green',
                datasets: [{
                    label: datasets.label,
                    data: datasets.data,
                    borderWidth: 1,
                    backgroundColor: datasets.backgroundColor
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
            }
        });
    }
}

class StatisticEvent {
    constructor() {
        this.adminDetailStatistic = new AdminDetailStatistics();
        this.adminSyntheticStatistic = new AdminSyntheticStatistic();
        this.init();
    }

    init() {
        this.btnFilterClick();
        this.applyDateRange();
        this.selectType();

        this.adminSyntheticStatistic.init().then();
    }

    btnFilterClick() {
        $('#filter-btn').on('click', async () => {
            const type = $('#statistic-type-select').val();
            switch (type) {
                case '1': {
                    await this.adminSyntheticStatistic.getData();
                    break;
                }
                case '2': {
                    await this.adminDetailStatistic.getData();
                    break;
                }
            }
        });
    }

    applyDateRange() {
        console.log('apply date range');
        const date = Utility.defaultDateRange();
        console.log(date)
        $('#from-date').val(date);
        $('#to-date').val(date);
    }

    selectType() {
        $('#statistic-type-select').on('change', async (e) => {
            console.log("change::", e.target.value);
            switch (e.target.value) {
                case '1': {
                    await this.adminSyntheticStatistic.init();
                    break;
                }
                case '2': {
                    await this.adminDetailStatistic.init();
                    break;
                }
            }
        });
    }

    static clear() {
        $('#overview').empty();
        $('#detail').empty();
        $('#table').empty();
        $('#other-filter').empty();
    }
}

class Utility {
    static defaultDateRange() {
        const date = new Date();
        const month = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1;
        const year = date.getFullYear();
        const day = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
        return `${year}-${month}-${day}`;
    };

    static formatDate(date_) {
        const date = new Date(date_);
        const month = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1;
        const year = date.getFullYear();
        const day = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
        return `${day}-${month}-${year}`;
    }
}

(() => {
    new StatisticEvent();
})();