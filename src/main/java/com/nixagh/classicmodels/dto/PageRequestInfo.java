package com.nixagh.classicmodels.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequestInfo {
  private Long pageNumber = 1L;
  private Long pageSize = 10L;
}
