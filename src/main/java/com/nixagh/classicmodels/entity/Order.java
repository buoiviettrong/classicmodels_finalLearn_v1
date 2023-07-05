package com.nixagh.classicmodels.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nixagh.classicmodels.entity.enums.ShippingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
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
@EqualsAndHashCode
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "orderNumber")
    private Long orderNumber;

    @Column(name = "orderDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    @Column(name = "requiredDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date requiredDate;

    @Column(name = "shippedDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shippedDate;

    @Column(name = "status")
    private String status = ShippingStatus.INPROC.getShippingStatus();

    @Column(name = "comments")
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
