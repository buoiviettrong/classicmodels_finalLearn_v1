package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.dto.AbstractRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFilterRequest extends AbstractRequest {
  private OrderFilter orderFilter = null;
}
