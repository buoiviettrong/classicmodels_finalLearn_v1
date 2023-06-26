package com.nixagh.classicmodels.dto.orders;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {
  private Long customerNumber;  // người tạo đơn là ai
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date requireDate;   // ngày yêu cầu

  private List<ProductOrder> products = new ArrayList<>(); // danh sách sản phẩm
}
