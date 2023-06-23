package com.nixagh.classicmodels.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PageResponseInfo {
  private Long currentPage;
  private Long pageSize;
  private Long totalElements;
  private Long totalPages;
  private Long totalElementOfCurrentPage;
}
