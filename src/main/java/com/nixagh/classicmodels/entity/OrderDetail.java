package com.nixagh.classicmodels.entity;

import com.nixagh.classicmodels.entity.embedded.OrderDetailsEmbed;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetail {

	@EmbeddedId
	private OrderDetailsEmbed id;

	@ManyToOne
	@JoinColumn(name = "orderNumber")
	@MapsId("orderNumber")
	private Order order;
	@ManyToOne
	@JoinColumn(name = "productCode")
	@MapsId("productCode")
	private Product product;

	@Column(name = "quantityOrdered", nullable = false, length = 10)
	private Long quantityOrdered;

	@Column(name = "priceEach", nullable = false, precision = 10)
	private Double priceEach;

	@Column(name = "orderLineNumber", nullable = false)
	private Integer orderLineNumber;
}
