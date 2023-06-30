package com.nixagh.classicmodels.dto.orders;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Builder
public class OrderUpdateRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shippedDate;
    private String status;
    private String comment;
}
