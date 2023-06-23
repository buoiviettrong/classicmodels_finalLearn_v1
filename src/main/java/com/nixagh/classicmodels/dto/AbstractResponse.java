package com.nixagh.classicmodels.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractResponse {
  private PageResponseInfo pageResponseInfo;
}
