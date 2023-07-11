package com.nixagh.classicmodels.dto.statistical;

import com.nixagh.classicmodels.entity.enums.ShippingStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatisticDTO {
    private Long orderNumber;
    private Date orderDate;
    private Date ShippedDate;
    private ShippingStatus status;
    private Long customerNumber;
    private String comment;
}
