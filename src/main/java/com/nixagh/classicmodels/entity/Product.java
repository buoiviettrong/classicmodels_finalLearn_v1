package com.nixagh.classicmodels.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
	@Id
	@Column(name = "productCode", length = 15)
	private String productCode;

	@Column(name = "productName", length = 70, nullable = false)
	private String productName;

	@ManyToOne(targetEntity = ProductLinee.class)
	@JoinColumn(name = "productLine")
	@JsonIgnore
	private ProductLinee productLine;

	@Column(name = "productScale", length = 10, nullable = false)
	private String productScale;

	@Column(name = "productVendor", length = 50, nullable = false)
	private String productVendor;

	@Column(name = "productDescription", nullable = false)
	private String productDescription;

	@Column(name = "quantityInStock", length = 15, nullable = false)
	private Integer quantityInStock;

	@Column(name = "buyPrice", nullable = false, precision = 10)
	private Double buyPrice;

	@Column(name = "MSRP", nullable = false, precision = 10)
	private Double MSRP;

	@JsonIgnore
	@OneToMany(mappedBy = "product")
	private Set<OrderDetail> orderDetail = new HashSet<OrderDetail>();
}

