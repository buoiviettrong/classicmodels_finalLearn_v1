package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.dto.AbstractResponse;
import com.nixagh.classicmodels.dto.page.PageResponseInfo;
import com.nixagh.classicmodels.entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderSearchResponse extends AbstractResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Order> orders = new ArrayList<>();

    public OrderSearchResponse(PageResponseInfo pageResponseInfo, List<Order> orders) {
        super(pageResponseInfo);
        this.orders = orders;
    }

    public OrderSearchResponse() {
        super();
    }
}
