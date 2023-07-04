package com.nixagh.classicmodels.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class AbstractRequest {
    private PageRequestInfo pageInfo;
}
