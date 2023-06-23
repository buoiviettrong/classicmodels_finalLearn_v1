package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.dto.AbstractResponse;
import com.nixagh.classicmodels.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderSearchResponse extends AbstractResponse {
  private List<Order> orders = new ArrayList<Order>();
}
