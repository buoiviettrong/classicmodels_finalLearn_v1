package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.entity.enums.DateType;
import com.nixagh.classicmodels.entity.enums.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderFilter {
  private Long customerNumber;
  private Long orderNumber;
  private ShippingStatus status;
  private Date orderDate;
  private Date requireDate;
  private Date shippedDate;
  private DateType orderDateType;
  private DateType requireDateType;
  private DateType shippedDateType;
}
