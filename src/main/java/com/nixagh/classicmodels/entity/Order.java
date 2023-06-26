package com.nixagh.classicmodels.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nixagh.classicmodels.entity.enums.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "orderNumber", insertable = false, updatable = false)
  private Long orderNumber;

  @Column(name = "orderDate", nullable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date orderDate;

  @Column(name = "requiredDate", nullable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date requiredDate;

  @Column(name = "shippedDate", length = 15)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date shippedDate;

  @Column(name = "status", nullable = false, length = 15)
  private String status = ShippingStatus.INPROC.getShippingStatus();

  @Column(name = "comments", length = 15)
  private String comments;

  @ManyToOne
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "customerNumber")
  @JsonIgnore
  private Customer customer;

  @OneToMany(mappedBy = "order")
  @JsonIgnore
  private Set<OrderDetail> orderDetail = new HashSet<OrderDetail>();
}
