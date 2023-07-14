const orderHistories = {
    lists: $('#orderHistoryTableBody'),
    order: {
        template: (order) => `
            <tr>
                <td>${order['orderNumber']}</td>
                <td>${order['orderDate']}</td>
                <td>${order['totalAmount']}</td>
                <td>${order['status']}</td>
                <td>
                    <a href="#" class="btn btn-primary" onclick="orderHistories.event.viewDetail('${order['orderNumber']}')">View</a>
                </td>
            </tr>
        `
    },
    init: async () => {
        orderHistories.render(await orderHistories.getOrders());
    },
    getOrders: async () => {
        const data = await callAPI.get(orderURL + "/history?customerNumber=" + user.customerNumber);
        if (data.message !== undefined) {
            alert(data.message);
            return;
        }
        return data;
    },
    render: (orders) => {
        if (orders === undefined) return;
        orders.forEach(order => {
            orderHistories.lists.append(orderHistories.order.template(order));
        });
    },
    event: {
        viewDetail: async (orderNumber) => {
            const data = await callAPI.get(orderURL + "/" + orderNumber + "/orderDetail");
            if (data.message !== undefined) {
                alert(data.message);
                return;
            }
            orderDetail.render(orderNumber, data);
        }
    },
};

const orderDetail = {
    orderID: $('#orderID'),
    lists: $('#orderDetailTableBody'),
    order: {
        template: (order) => `
            <tr>
                <td>${order['productCode']}</td>
                <td>${order['productName']}</td>
                <td>${order['quantityOrdered']}</td>
                <td>${order['priceEach']}</td>
            </tr>
        `
    },
    render: (orderId, orders) => {
        if (orders === undefined) return;
        orderDetail.lists.empty();
        orderDetail.orderID.val(orderId);
        orders.forEach(order => {
            orderDetail.lists.append(orderDetail.order.template(order));
        });
    },
};
(async () => {
    await orderHistories.init();
})();