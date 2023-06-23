package com.nixagh.classicmodels.entity.embedded;

import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsEmbed implements Serializable {
	private Long orderNumber;
	private String productCode;
}
