package com.nixagh.classicmodels.dto;

import com.nixagh.classicmodels.dto.page.PageResponseInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResponse implements Serializable {
    private PageResponseInfo pageResponseInfo;
}
