const globalProductScale = [{scale: '1:10', value: 10}, {scale: '1:12', value: 12}, {
    scale: '1:18',
    value: 18
}, {scale: '1:24', value: 24}, {scale: '1:32', value: 32}, {scale: '1:50', value: 50}, {scale: '1:700', value: 700}]
const globalProductVendors = ['Min Lin Diecast', 'Classic Metal Creations', 'Highway 66 Mini Classics', 'Red Start Diecast', 'Motor City Art Classics', 'Second Gear Diecast', 'Autoart Studio Design', 'Welly Diecast Productions', 'Unimax Art Galleries', 'Studio M Art Models', 'Exoto Designs', 'Gearbox Collectibles', 'Carousel DieCast Legends']

const modalGenerate = {
    alert: {
        title: 'Alert',
        message: 'Alert Message',
        typeList: {
            "success": {
                header: 'Well done!',
                message: 'Create New Product Success',
                class: 'alert-success'
            },
            "danger": {
                header: 'Oh snap!',
                message: 'Create New Product Fail',
                class: 'alert-danger'
            }
        },
        body: (type) => `
            <div class="alert ${type.class}" role="alert">
                <span class="alert-heading h4">${type.header}</span>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <span>${type.message}</span>
            </div>
        `,

    },
    showAlert: function (type, message) {
        const alertMessage = modalGenerate.alert
        let alertType = alertMessage.typeList[type];
        alertType.message = message;
        const alert_ = $('#alert_');
        alert_.empty();
        alert_.append(alertMessage.body(alertType)).show();
    },
    emptyAlert: function () {
        $('#alertProductName').empty().hide();
        $('#alertProductPrice').empty().hide();
        $('#alertProductCode').empty().hide();
        $('#alertProductMsrp').empty().hide();
        $('#alert_').empty().hide();
    },
    addProduct: {
        title: 'Add Product',
        content: `
            <div class="modal fade" id="addProduct" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
              <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLongTitle">${this.title}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                  <div class="modal-body">
                  <!--productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP-->
                    <div class="row">
                        <div class="col-6">
                            <!-- productCode -->
                            <div class="form-group">
                                <label for="productCode">Product Code (4 number)</label>
                                <input type="text" class="form-control" id="productCode" placeholder="Product Code">
                                <!-- alert message -->
                                <p class="alert alert-danger fade show" role="alert" id="alertProductCode"></p>
                            </div>
                            <div class="form-group">
                                <label for="productName">Product Name</label>
                                <input type="text" class="form-control" id="productName" placeholder="Product Name">
                                <!-- alert message -->
                                <p class="alert alert-danger fade show" role="alert" id="alertProductName"></p>
                            </div>
                            <div class="form-group">
                                <label for="productPrice">Product Price</label>
                                <input type="text" class="form-control" id="productPrice" placeholder="Product Price">
                                <!-- alert message -->
                                <p class="alert alert-danger fade show" role="alert" id="alertProductPrice"></p>
                            </div>
                           <div class="form-group">
                                <label for="productMsrp">Product Msrp</label>
                                <input type="text" class="form-control" id="productMsrp" placeholder="Product Msrp">
                                <!-- alert message -->
                                <p class="alert alert-danger fade show" role="alert" id="alertProductMsrp"></p>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <label for="productDescription">Product Description</label>
                                <textarea class="form-control" id="productDescription" placeholder="Product Description"></textarea>
                            </div>
                            <div class="form-group">
                                <label for="productScale">Product Scale</label>
                                <select class="form-control" id="productScale">
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="productVendor">Product Vendor</label>
                                <select class="form-control" id="productVendor">
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="productLine">Product Line</label>
                                <select class="form-control" id="productLine">
                                </select>
                            </div>
                        </div>
                    </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="saveNewProduct">Save changes</button>
                  </div>
                </div>
              </div>
            </div>      
        `,
        productInfo: () => {
            return {
                productCode: $('#productCode').val(),
                productName: $('#productName').val(),
                buyPrice: $('#productPrice').val(),
                productDescription: $('#productDescription').val(),
                productScale: $('#productScale').val(),
                productVendor: $('#productVendor').val(),
                productLine: $('#productLine').val(),
                msrp: $('#productMsrp').val()
            }
        },
        event: {
            show: function () {
                $('#addProduct')["modal"]('show');
            },
            hide: function () {
                $('#addProduct')["modal"]('hide');
            },
            toggle: function () {
                $('#addProduct')["modal"]('toggle');
            },
            destroy: function () {
                $('#addProduct').remove();
            },
            check: function () {
                modalGenerate.emptyAlert();
                const productName = $('#productName');
                const productPrice = $('#productPrice');
                const productCode = $('#productCode');
                const productMsrp = $('#productMsrp');

                if (productCode.val() === '') {
                    $('#alertProductCode').append('Product Code is required').show();
                }
                if (productName.val() === '') {
                    $('#alertProductName').append('Product Name is required').show();
                }
                if (productPrice.val() === '') {
                    $('#alertProductPrice').append('Product Price is required').show();
                }
                if (productMsrp.val() === '') {
                    $('#alertProductMsrp').append('Product Msrp is required').show();
                }

                return productName.val() !== '' && productPrice.val() !== '' && productCode.val() !== '' && productMsrp.val() !== '';
            },
            save: function () {
                const product = modalGenerate.addProduct.productInfo();
                if (!modalGenerate.addProduct.event.check()) return;

                callAPI.post(productURL, product).then(async (res) => {
                    // check fail message
                    if (res.message !== undefined && res.message !== null) {
                        modalGenerate.showAlert('danger', res.message);
                        return;
                    }

                    // close modal
                    modalGenerate.addProduct.event.toggle();
                    // modalGenerate.addProduct.event.destroy();

                    // show alert
                    modalGenerate.showAlert('success', 'Add product success');

                    await products.reload();
                });
            }
        },
        init: async function () {
            $('#modal').empty().append(this.content);
            modalGenerate.emptyAlert();

            const productScaleSelect = $('#productScale');
            const productVendorSelect = $('#productVendor');
            const productLineSelect = $('#productLine');

            globalProductScale.forEach((item) => {
                productScaleSelect.append(`<option value="${item.value}">${item.scale}</option>`);
            });

            globalProductVendors.forEach((item) => {
                productVendorSelect.append(`<option value="${item}">${item}</option>`);
            });

            const productLine = await callAPI.get(productLineURL + "/select");
            productLine.forEach((item) => {
                productLineSelect.append(`<option value="${item}">${item}</option>`);
            });
            $('#saveNewProduct').click(this.event.save);
        }
    },
    editProduct: {
        content: `
            <div class="modal fade" id="editProduct" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
              <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLongTitle">Edit Product</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                  <div class="modal-body">
                  <!--productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP-->
                    <div class="row">
                        <div class="col-6">
                            <!-- productCode -->
                            <div class="form-group">
                                <label for="productCode">Product Code</label>
                                <input type="text" class="form-control" id="productCode" placeholder="Product Code" disabled>
                                <!-- alert message -->
                                <p class="alert alert-danger fade show" role="alert" id="alertProductCode"></p>
                            </div>
                            <div class="form-group">
                                <label for="productName">Product Name</label>
                                <input type="text" class="form-control" id="productName" placeholder="Product Name">
                                <!-- alert message -->
                                <p class="alert alert-danger fade show" role="alert" id="alertProductName"></p>
                            </div>
                            <div class="form-group">
                                <label for="productPrice">Product Price</label>
                                <input type="text" class="form-control" id="productPrice" placeholder="Product Price">
                                <!-- alert message -->
                                <p class="alert alert-danger fade show" role="alert" id="alertProductPrice"></p>
                            </div>
                           <div class="form-group">
                                <label for="productMsrp">Product Msrp</label>
                                <input type="text" class="form-control" id="productMsrp" placeholder="Product Msrp">
                                <!-- alert message -->
                                <p class="alert alert-danger fade show" role="alert" id="alertProductMsrp"></p>
                            </div>
                           <div class="form-group">
                                <label for="productQuantity">Product Quantity</label>
                                <input type="text" class="form-control" id="productQuantity" placeholder="Product Quantity">
                                <!-- alert message -->
                                <p class="alert alert-danger fade show" role="alert" id="alertProductQuantity"></p>
                           </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <label for="productDescription">Product Description</label>
                                <textarea class="form-control" id="productDescription" placeholder="Product Description"></textarea>
                            </div>
                            <div class="form-group">
                                <label for="productScale">Product Scale</label>
                                <select class="form-control" id="productScale">
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="productVendor">Product Vendor</label>
                                <select class="form-control" id="productVendor">
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="productLine">Product Line</label>
                                <select class="form-control" id="productLine">
                                </select>
                            </div>
                        </div>
                    </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="saveEditProduct">Save changes</button>
                  </div>
                </div>
              </div>
            </div> 
        `,
        productInfo: () => {
            return {
                productCode: $('#productCode').val(),
                productName: $('#productName').val(),
                buyPrice: $('#productPrice').val(),
                productDescription: $('#productDescription').val(),
                productScale: $('#productScale').val(),
                productVendor: $('#productVendor').val(),
                productLine: $('#productLine').val(),
                msrp: $('#productMsrp').val(),
                quantityInStock: $('#productQuantity').val()
            }
        },
        addDataToModal: (product) => {
            $('#productCode').val(product.productCode);
            $('#productName').val(product.productName);
            $('#productPrice').val(product.buyPrice);
            $('#productDescription').val(product.productDescription);
            $('#productMsrp').val(product.msrp);
            $('#productQuantity').val(product.quantityInStock);
        },
        event: {
            edit: function () {
                const product = modalGenerate.editProduct.productInfo();

                callAPI.put(productURL + "/" + product.productCode, product).then(async (res) => {
                    // check fail message
                    if (res.message !== undefined && res.message !== null) {
                        modalGenerate.showAlert('danger', res.message);
                        return;
                    }

                    // close modal
                    modalGenerate.editProduct.event.toggle();
                    // modalGenerate.editProduct.event.destroy();

                    // show alert
                    modalGenerate.showAlert('success', 'Edit product success');

                    await products.reload();
                });
            },
            show: () => {
                $('#editProduct')["modal"]('show');
            },
            toggle: () => {
                $('#editProduct')["modal"]('toggle');
            }
        },
        init: async function (id) {
            const data = await callAPI.get(productURL + "/" + id);
            $('#modal').empty().append(this.content);
            modalGenerate.emptyAlert();
            this.addDataToModal(data);

            const productScaleSelect = $('#productScale');
            const productVendorSelect = $('#productVendor');
            const productLineSelect = $('#productLine');

            globalProductScale.forEach((item) => {
                if (item.value === data.productScale)
                    productScaleSelect.append(`<option value="${item.value}" selected>${item.scale}</option>`);

                else
                    productScaleSelect.append(`<option value="${item.value}">${item.scale}</option>`);

            });

            globalProductVendors.forEach((item) => {
                if (item === data.productVendor)
                    productVendorSelect.append(`<option value="${item}" selected>${item}</option>`);
                else
                    productVendorSelect.append(`<option value="${item}">${item}</option>`);
            });

            const productLine = await callAPI.get(productLineURL + "/select");
            productLine.forEach((item) => {
                if (item === data.productLine)
                    productLineSelect.append(`<option value="${item}" selected>${item}</option>`);
                else
                    productLineSelect.append(`<option value="${item}">${item}</option>`);
            });
            $('#saveEditProduct').click(this.event.edit);
        }
    }
}
const loadScale = () => {
    globalProductScale.forEach((item) => {
        $('#product-scale-filter').append(`<option value="${item.value}">${item.scale}</option>`);
    });
}

const loadVendor = () => {
    globalProductVendors.forEach((item) => {
        $('#product-vendor-filter').append(`<option value="${item}">${item}</option>`);
    });
}
const loadProductLine = async () => {
    const productLine = await callAPI.get(productLineURL + "/select");
    productLine.forEach((item) => {
        $('#product-line-filter').append(`<option value="${item}">${item}</option>`);
    });
}
// Path: src\main\resources\static\js\product.js
const products = {
    callData: async (filterRequest, pageInfo) => {
        const request = {
            filter: filterRequest,
            pageInfo: pageInfo
        }
        return await callAPI.post(productURL + "/filter", request);
    },
    init: async function () {
        await loadProductLine();
        loadScale();
        loadVendor();

        await this.filter();
    },
    updatePageInfo: (data) => {
        $('#current-page').val(data["currentPage"]);
        $('#total-page').val(data["totalPages"]);
        $('#total-record').val(data["totalElements"]);
        $('#record-per-page').val(data["pageSize"]);
    },
    reload: async function () {
        const productScale = $('#product-scale-filter');
        const productVendor = $('#product-vendor-filter');
        const productLine = $('#product-line-filter');

        // empty all filter
        productScale.empty();
        productVendor.empty();
        productLine.empty();

        // all filter
        productScale.append(`<option value="0">All</option>`);
        productVendor.append(`<option value="All">All</option>`);
        productLine.append(`<option value="All>All</option>`);

        // empty page info
        $('#current-page').val(1);
        $('#total-page').val(1);
        $('#total-record').val(0);
        $('#record-per-page').val(10);

        // reload filter
        await this.init();
    },
    addDataToTable: function (data) {
        const table = $('#product-table');
        table.empty();
        data.forEach((item) => {
            let row = `<tr>
                            <td>${item.productCode}</td>
                            <td>${item.productName}</td>
                            <td>${item.productLine}</td>
                            <td>${item.productScale}</td>
                            <td>${item.productVendor}</td>
                            <td>${item.productDescription}</td>
                            <td>${item.quantityInStock}</td>
                            <td>${item.buyPrice}</td>
                            <td>${item.msrp}</td>
                            <td>
                                <button class="btn btn-primary" onclick="products.editProduct('${item.productCode}')">Edit</button>
                                <button class="btn btn-danger"
                                        data-toggle="modal"
                                        data-target="#confirm-delete"
                                        data-record-title="${item.productCode}"
                                        data-record-id="${item.productCode}">Delete</button>
                            </td>
                        </tr>`;
            table.append(row);
        });
    },
    filter: async function () {
        const filterRequest = {
            // productCode: $('#productCodeFilter'),
            // productName: $('#productNameFilter'),
            productLine: $('#product-line-filter').val(),
            productScale: $('#product-scale-filter').val(),
            productVendor: $('#product-vendor-filter').val(),
            // productDescription: $('#product-description-filter')
            quantityInStock: {
                min: $('#quantity-in-stock-filter-min').val(),
                max: $('#quantity-in-stock-filter-max').val()
            }
        };
        const pageInfo = {
            pageNumber: $('#current-page').val(),
            pageSize: $('#record-per-page').val(),
        }
        const data = await this.callData(filterRequest, pageInfo);

        if (data.message !== undefined && data.message !== null) {
            modalGenerate.showAlert('danger', data.message);
            return;
        }

        this.updatePageInfo(data["pageResponseInfo"]);
        this.addDataToTable(data["products"]);
    },
    search: function () {

    },
    editProduct: async function (id) {
        await modalGenerate.editProduct.init(id);
        modalGenerate.editProduct.event.show();
    },
    deleteProduct: async function (id) {
        const data = await callAPI.delete(productURL + "/" + id);

        if (data.message !== undefined && data.message !== null) {
            modalGenerate.showAlert('danger', data.message);
            return;
        }

        modalGenerate.showAlert('success', 'Delete product successfully');

        await products.reload();
    },
    addProduct: async function () {
        await modalGenerate.addProduct.init();
        modalGenerate.addProduct.event.show();
    },
};

(async () => {
    await products.init();

    const confirmDelete = $('#confirm-delete');
// Bind click to OK button within popup
    confirmDelete.on('click', '.btn-ok', function (e) {
        const $modalDiv = $(e.delegateTarget);
        const id = $(this).data('recordId');
        $modalDiv.addClass('loading');
        products.deleteProduct(id).then(() => {
            $modalDiv["modal"]('hide').removeClass('loading');
        });
    });

// Bind to modal opening to set necessary data properties to be used to make request
    confirmDelete.on('show.bs.modal', function (e) {
        const data = $(e.relatedTarget).data();
        $('.title', this).text(data["recordTitle"]);
        $('.btn-ok', this).data('recordId', data["recordId"]);
    });
})();

// next page
const nextPage = async () => {
    const currentPage_ = $('#current-page');
    const currentPage = parseInt(currentPage_.val());
    const totalPage = parseInt($('#total-page').val());
    if (currentPage >= totalPage) return;
    currentPage_.val(currentPage + 1);
    await products.filter();
}
// previous page
const previousPage = async () => {
    const currentPage_ = $('#current-page');
    const currentPage = parseInt(currentPage_.val());
    if (currentPage <= 1) return;
    currentPage_.val(currentPage - 1);
    await products.filter();
}

