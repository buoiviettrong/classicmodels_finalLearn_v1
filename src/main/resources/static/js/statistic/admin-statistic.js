class AdminStatistic {

    constructor() {
        this.init().then();
    }

    async init() {
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
        this.$detailData = [{
            month: 1,
            totalInvoice: 0,
            totalMoney: 0,
            totalSoldProduct: 0
        }];
        this.$overview = $('#overview');
        this.$detail = $('#detail');
        await this.getData();
    }

    async getData() {
        const url = statisticURL + "/admin-statistical";
        const request = {
            from: $('#from-date').val(),
            to: $('#to-date').val(),
            type: ''
        }
        console.log(request);
        const data = await callAPI.post(url, request);
        this.$overviewData = data.overview;
        this.$detailData = data.detail;
        this.setOverview();
        this.setDetails();
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
            <div class="row d-flex justify-content-between">
                <div class="col">Hóa Đơn Thu Được Cao Nhất</div>
                <div class="col">
                    <div class="row">
                        <div class="col">Mã hóa đơn</div>
                        <div class="col">${this.$overviewData.overviewTop.invoice.orderNumber}</div>
                    </div>
                    <div class="row">
                        <div class="col">Ngày đặt</div>
                        <div class="col">${this.$overviewData.overviewTop.invoice.orderDate}</div>
                        
                    </div>
                    <div class="row">
                        <div class="col">Tổng tiền</div>
                        <div class="col">${this.$overviewData.overviewTop.invoice.totalMoney}</div>
                    </div>  
                </div>
            </div>
            <hr>
            <div class="row d-flex justify-content-between">
                <div class="col">Sản Phẩm Bán Chạy Nhất</div>
                <div class="col">
                    <div class="row">
                        <div class="col">Mã sản phẩm</div>
                        <div class="col">${this.$overviewData.overviewTop.product.productCode}</div>
                    </div>
                    <div class="row">
                        <div class="col">Tên sản phẩm</div>
                        <div class="col">${this.$overviewData.overviewTop.product.productName}</div>
                    </div>
                    <div class="row">
                        <div class="col">Số lượng bán</div>
                        <div class="col">${this.$overviewData.overviewTop.product.quantity}</div>
                    </div>
                </div>
            </div>
            <hr>
            <div class="row d-flex justify-content-between">
                <div class="col">Dòng Sản Phẩm Đặt Mua Nhiều Nhất</div>
                <div class="col">
                    <div class="row">
                        <div class="col">Mã dòng sản phẩm</div>
                        <div class="col">${this.$overviewData.overviewTop.productLine.productLineCode}</div>
                    </div>
                    <div class="row">
                        <div class="col">Số lượng đặt mua</div>
                        <div class="col">${this.$overviewData.overviewTop.productLine.quantity}</div>
                    </div>
                </div>
            </div>
            <hr>
            <div class="row d-flex justify-content-between">
                <div class="col">Khách Hàng Đặt Mua Nhiều Nhất</div>
                <div class="col">
                    <div class="row">
                        <div class="col">Mã khách hàng</div>
                        <div class="col">${this.$overviewData.overviewTop.customer.customerNumber}</div>
                    </div>
                    <div class="row">
                        <div class="col">Tên khách hàng</div>
                        <div class="col">${this.$overviewData.overviewTop.customer.customerName}</div>
                    </div>
                    <div class="row">
                        <div class="col">Số lượng đặt mua</div>
                        <div class="col">${this.$overviewData.overviewTop.customer.quantityInvoice}</div>
                    </div>
                    <div class="row">
                        <div class="col">Tổng tiền</div>
                        <div class="col">${this.$overviewData.overviewTop.customer.totalMoney}</div>
                    </div>
                </div>
            </div>
        </div>
        `
        this.$overview.append(overviewTemplateTotal);
        this.$overview.append(overviewTemplateTop);
    };

    setDetails() {
        this.$detail.empty();
        const detailTemplate = `
        <canvas style="position: absolute;" id="myChart" width="400" height="400"></canvas>
        `;
        this.$detail.append(detailTemplate);
        const ctx = document.getElementById('myChart').getContext('2d');

        const labels = this.$detailData.map((item) => {
            return item.date;
        });
        const datasets = {
            label: 'Doanh thu',
            data: this.$detailData.map((item) => {
                return item.totalMoney;
            })
        }
        Chart_.createChart(ctx, labels, datasets);
    }
}

class Chart_ {
    static createChart(ctx, labels, datasets) {
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: datasets.label,
                    data: datasets.data,
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
}

class StatisticEvent {
    constructor() {
        this.btnFilterClick();
        this.applyDateRange();
        this.adminStatistic = new AdminStatistic();
    }

    btnFilterClick() {
        $('#filter-btn').on('click', async () => {
            await this.adminStatistic.getData();
        });
    }

    applyDateRange() {
        console.log('apply date range');
        const date = Utility.defaultDateRange();
        console.log(date)
        $('#from-date').val(date);
        $('#to-date').val(date);
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

    static formatDate(date) {
        const month = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1;
        const year = date.getFullYear();
        const day = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
        return `${day}-${month}-${year}`;
    }
}

(() => {
    new StatisticEvent();
})();