package com.nixagh.classicmodels.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeeNumber")
    private Long employeeNumber;

    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "reportsTo", nullable = false)
    private Long reportsTo;

    @Column(name = "jobTitle", nullable = false, length = 50)
    private String jobTitle;

    @ManyToOne(targetEntity = Office.class)
    @JoinColumn(name = "officeCode")
    private Office officeCode;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reportsTo")
    @JsonIgnore
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "salesRepEmployeeNumber", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Customer> customersList = new ArrayList<>();
}
