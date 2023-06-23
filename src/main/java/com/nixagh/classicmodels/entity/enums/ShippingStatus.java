package com.nixagh.classicmodels.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShippingStatus {
	SHIPPED("Shipped"),
	RECEIVED("Received"),
	DELIVERING("Delivering"),
	HOLD("On Hold"),
	CANCELLED("Cancelled"),
	DISPUTED("Disputed"),
  DELIVERED("Delivered"),
	RESOLVED("Resolved"),
	INPROC("In Process");
	@Getter
	private final String ShippingStatus;
}
