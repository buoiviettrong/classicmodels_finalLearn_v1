package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.entity.enums.ShippingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Builder
public class OrderUpdateRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shippedDate;
    private ShippingStatus status;
    private String comment;
}
