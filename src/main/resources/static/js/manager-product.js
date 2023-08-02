

const defaultImageSource = 'data:image/svg+xml;charset=UTF-8,%3Csvg%20width%3D%22362%22%20height%3D%22200%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%20362%20200%22%20preserveAspectRatio%3D%22none%22%3E%3Cdefs%3E%3Cstyle%20type%3D%22text%2Fcss%22%3E%23holder_1894e206bf4%20text%20%7B%20fill%3Argba(255%2C255%2C255%2C.75)%3Bfont-weight%3Anormal%3Bfont-family%3AHelvetica%2C%20monospace%3Bfont-size%3A18pt%20%7D%20%3C%2Fstyle%3E%3C%2Fdefs%3E%3Cg%20id%3D%22holder_1894e206bf4%22%3E%3Crect%20width%3D%22362%22%20height%3D%22200%22%20fill%3D%22%23777%22%3E%3C%2Frect%3E%3Cg%3E%3Ctext%20x%3D%22134.953125%22%20y%3D%22108.1%22%3E362x200%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E'
const products = {
    lists: {},
    body: {
        lists: $('#products-lists'),
        card: (product) => `
            <div class="col-md-4 mb-3 col-lg-4 d-flex align-items-stretch">
                <div class="card">
                    <img class="card-img-top" src="${product.image || defaultImageSource}" alt="Card image cap">
                    <div class="card-body">
                        <!-- product name -->
                        <h5 class="card-title">${product['productName'] || 'This is product Name'}</h5>
                        <!-- product description -->
                        <p class="card-text">${product['productDescription'] || 'This is product description..'}</p>
                        <!-- product price -->
                        <div class="d-flex justify-content-between align-items-center float-end">
                            <div class="btn-group">
                                <h5>Price: ${product['msrp'] || 0} / each</h5>
                            </div>
                            <div class="btn-group">
                                ${product['quantityInStock'] > 10 ? `<h5 class="text-success">In Stock</h5>` : `<h5 class="text-danger">Out Of Stock</h5>`}
                            </div>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-between align-items-center">
                        <a href="#" class="btn btn-sm btn-outline-secondary" onclick="products.event.view('${product.productCode}')">View</a>
                        <a href="#" class="btn btn-sm btn-outline-secondary" onclick="products.event.addToCart('${product.productCode}')">Add To Cart</a>
                    </div>
                </div>
            </div>  
        `
    },
    init: async function () {
        await this.load();

        // add event listener for next page button
        await this.event.nextPage();
        // add event listener for previous page button
        await this.event.previousPage();
    },
    event: {
        search: async function () {
            await products.load();
        },
        view: function (productCode) {
            window.location.href = `${productURL}/${productCode}`;
        },
        addToCart: function (productCode) {
            carts.add(products.lists[productCode]);
        },
        emptyLists: function () {
            products.lists = {};
            products.body.lists.empty();
        },
        nextPage: async function () {
            const cP = $('#current-page');
            const currentPage = parseInt(cP.text());
            const totalPage = parseInt($('#total-page').text());
            if (currentPage < totalPage) {
                cP.text(currentPage + 1);
                await products.load();
            }
        },
        previousPage: async function () {
            const cP = $('#current-page');
            const currentPage = parseInt(cP.text());
            if (currentPage > 1) {
                cP.text(currentPage - 1);
                await products.load();
            }
        }
    },
    render: function (products_) {
        this.event.emptyLists();
        products_.forEach(product => {
            this.body.lists.append(this.body.card(product));
            this.lists[product.productCode] = product;
        });
    },
    load: async function () {
        const url = '/api/v1/products/manager-search'
        const request = {
            pageInfo: {
                pageNumber: $('#current-page').text() || 1,
                pageSize: 6
            },
            search: $('#searchInput').val(),
            productLine: $('#product-line-select').val()
        }
        const data = await callAPI.post(url, request);
        // update page info
        this.updatePageInfo(data['pageResponseInfo']);
        // render products
        this.render(data['products']);
    },
    updatePageInfo: function (pageInfo) {
        $('#current-page').text(pageInfo['currentPage']);
        $('#total-page').text(pageInfo['totalPages']);
    }
};

const carts = {
    lists: {},
    body: {
        lists: $('#carts-lists'),
        product: (product) => `
            <tr>
                <td>
                    <div class="media">
                        <div class="d-flex">
                            <!-- scale down image -->
                            <img src="${product.image || defaultImageSource}" alt="" style="width: 150px; height: 100px;"/>
                        </div>
                    </div>
                </td>
                <td>
                    <h5>${product['productName'] || 'This is product Name'}</h5>
                </td>
                <td class="ms-3">
                    <h5>${product['msrp'] || 0}</h5>
                </td>
                <td>
                    <div class="product_count">
                        <button
                            onclick="carts.event.decreaseQuantity('${product.productCode}')"
                            class="reduced items-count button-minus border rounded-circle  icon-shape icon-sm mx-1" type="button">
                            -
                        </button>
                        <input type="text" name="qty" id="${product.productCode} input" maxlength="12" value="${product['quantity'] || 0}" title="Quantity:"
                            class="input-text qty" onchange="carts.event.change('${product.productCode}')"/>
                        <button
                            onclick="carts.event.increaseQuantity('${product.productCode}')"
                            class="increase items-count button-plus border rounded-circle icon-shape icon-sm" type="button">
                            +
                        </button>
                    </div>
                </td>
                <td>
                    <h5>${(product['msrp'] * product['quantity']).toFixed(2) || 0}</h5>
                </td>
                <!-- remove product from cart -->
                <td>
                    <a href="#" onclick="carts.event.remove('${product.productCode}')">
                        Remove
                    </a>
                </td>
            </tr>
        `
    },
    event: {
        increaseQuantity: function (productCode) {
            carts.lists[productCode]['quantity'] += 1;
            carts.render();
        },
        decreaseQuantity: function (productCode) {
            if (carts.lists[productCode]['quantity'] > 1) {
                carts.lists[productCode]['quantity'] -= 1;
                carts.render();
            }
        },
        change: function (productCode) {
            const quantity = parseInt($(`[id='${productCode} input']`).val());
            if (quantity > 0) {
                carts.lists[productCode]['quantity'] = quantity;
            }
            carts.render();
        },
        remove: function (productCode) {
            delete carts.lists[productCode];
            carts.render();
        }
    },
    init: function () {
        $('#checkoutBtn').click(function () {
            carts.checkout();
        })
    },
    render: function () {
        this.body.lists.empty();
        const totalPrice = $('#total-price');
        totalPrice.text(0);
        let sum = 0;
        for (let key in this.lists) {
            this.body.lists.append(this.body.product(this.lists[key]));
            sum += this.lists[key]['msrp'] * this.lists[key]['quantity'];
        }
        totalPrice.text(sum.toFixed(2));
    },
    add: function (product) {
        if (this.lists[product.productCode]) {
            this.lists[product.productCode]['quantity'] += 1;
        } else {
            this.lists[product.productCode] = product;
            this.lists[product.productCode]['quantity'] = 1;
        }
        this.render();
    },
    checkout: function () {
        // check if cart is empty
        if (Object.keys(this.lists).length === 0) {
            alert('Your cart is empty!');
            return;
        }

        // checkout
        const url = '/api/v1/orders/checkout';
        const postProducts = [];
        for (let key in this.lists) {
            postProducts.push({
                productCode: this.lists[key]['productCode'],
                quantity: this.lists[key]['quantity'],
                price: this.lists[key]['msrp']
            });
        }

        const request = {
            products: postProducts,
            customerNumber: user['customerNumber'],
            requireDate: new Date().toISOString().slice(0, 10)
        };
        console.log(request);
        callAPI.post(url, request)
            .then(async data => {
                console.log(data);
                if (data.message !== undefined) {
                    alert(data.message);
                    return;
                }

                const url_ = '/api/v1/payments/create-payment';
                const request_ = {
                    customerNumber: user['customerNumber'],
                    orderNumber: data['orderNumber'],
                    amount: $('#total-price').text()
                };
                const data_ = await callAPI.post(url_, request_);

                if (data_['status'] === 'Ok') {
                    window.location.href = data_['paymentUrl'];
                    alert('Checkout successfully!');
                    this.lists = {};
                    this.render();
                }

            })
    }
};

const loadProductLine = async () => {
    const url = productLineURL + '/select';
    const data = await callAPI.get(url);
    const productLineSelect = $('#product-line-select');
    productLineSelect.empty();
    productLineSelect.append(`<option value="all">All</option>`);
    data.forEach(productLine => {
            productLineSelect.append(`<option value="${productLine}">${productLine}</option>`);
        }
    );
};

(async () => {
    await products.init();
    carts.init();
    await loadProductLine();

    const socket = new Socket_(async () => {
        await products.load();
    });
})();