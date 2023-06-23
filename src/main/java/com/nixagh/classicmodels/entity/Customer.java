package com.nixagh.classicmodels.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customerNumber", nullable = false)
	private Long customerNumber;

	@Column(name = "customerName", nullable = false, length = 50)
	private String customerName;

	@Column(name = "contactLastName", nullable = false, length = 50)
  private String contactLastName;

	@Column(name = "contactFirstName", nullable = false, length = 50)
	private String contactFirstName;

	@Column(name = "phone", nullable = false, length = 50)
	private String phone;

	@Column(name = "addressLine1", nullable = false, length = 50)
	private String addressLine1;

	@Column(name = "addressLine2", length = 50)
	private String addressLine2;

	@Column(name = "city", nullable = false, length = 50)
	private String city;

	@Column(name = "state", length = 50)
	private String state;

	@Column(name = "postalCode", length = 15)
	private String postalCode;

	@Column(name = "country", nullable = false, length = 50)
	private String country;

	@ManyToOne
	@JoinColumn(name = "salesRepEmployeeNumber") // thông qua khóa ngoại
//	@JsonIgnore
	private Employee salesRepEmployeeNumber;

	@Column(name = "creditLimit", precision = 10)
	private Double creditLimit;

	@OneToMany(targetEntity = Order.class, mappedBy = "customer")
	@JsonIgnore
	private List<Order> ordersList = new ArrayList<>();

	@OneToMany(mappedBy = "customer", targetEntity = Payment.class)
	@JsonIgnore
	private Set<Payment> payments = new HashSet<>();

}
