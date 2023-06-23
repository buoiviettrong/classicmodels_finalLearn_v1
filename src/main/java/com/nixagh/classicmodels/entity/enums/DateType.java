package com.nixagh.classicmodels.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DateType {
  EQ("equal"),
  LT("less than"),
  LTE("less than or equal to"),
  GT("greater than"),
  GTE("greater than or equal to");
  @Getter
  private final String DateType;
}
