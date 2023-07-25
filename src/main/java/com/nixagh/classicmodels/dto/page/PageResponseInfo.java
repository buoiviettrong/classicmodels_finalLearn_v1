package com.nixagh.classicmodels.dto.page;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseInfo implements Serializable {
    private Long currentPage;
    private Long pageSize;
    private Long totalElements;
    private Long totalPages;
    private Long totalElementOfCurrentPage;
}
