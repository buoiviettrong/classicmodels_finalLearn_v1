package com.nixagh.classicmodels.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {


  @Id
  @Column(name = "checkNumber", nullable = false, length = 50)
  private String checkNumber;

  @Column(name = "paymentDate", nullable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date paymentDate;

  @Column(name = "amount", nullable = false, precision = 10)
  private Double amount;

  @ManyToOne
  @JoinColumn(name = "customerNumber", nullable = false)
  private Customer customer;
}
