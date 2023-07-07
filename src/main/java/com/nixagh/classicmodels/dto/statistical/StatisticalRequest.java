package com.nixagh.classicmodels.dto.statistical;

import com.nixagh.classicmodels.dto.AbstractRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Getter
@Setter
@Builder
@ToString
public class StatisticalRequest extends AbstractRequest {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date to;
}
