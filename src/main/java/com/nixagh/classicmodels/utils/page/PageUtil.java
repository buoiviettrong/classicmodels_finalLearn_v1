package com.nixagh.classicmodels.utils.page;

import com.nixagh.classicmodels.dto.page.PageResponseInfo;

public class PageUtil {
    public static PageResponseInfo getResponse(
            Long currentPage,
            Long pageSize,
            Long totalElements,
            Long totalElementOfCurrenPage
    ) {
        pageSize = pageSize == null || pageSize == 0 ? 10 : pageSize;
        Long totalPage = totalElements % pageSize == 0 ? totalElements / pageSize : totalElements / pageSize + 1;
        return PageResponseInfo.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalPages(totalPage)
                .totalElements(totalElements)
                .totalElementOfCurrentPage(totalElementOfCurrenPage)
                .build();
    }
}
