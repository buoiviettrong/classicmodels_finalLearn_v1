package com.nixagh.classicmodels.dto.orderDetail;

import com.nixagh.classicmodels.dto.product.ProductDTO;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.entity.embedded.OrderDetailsEmbed;

import java.util.List;

public record OrderDetailByOrderNumber(
    OrderDetailsEmbed id,
    Order order,
    List<ProductDTO> product
) {
}

