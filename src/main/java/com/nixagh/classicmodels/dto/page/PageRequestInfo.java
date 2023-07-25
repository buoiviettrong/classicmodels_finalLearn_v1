package com.nixagh.classicmodels.dto.page;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PageRequestInfo {
    private Long pageNumber = 1L;
    private Long pageSize = 10L;
}
