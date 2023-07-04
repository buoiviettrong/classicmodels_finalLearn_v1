package com.nixagh.classicmodels.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_lines")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductLinee implements Serializable {
    @Id
    @Column(name = "productLine", length = 50)
//  @JsonProperty("productLine")
    private String productLine;

    @Column(name = "textDescription", length = 4000)
    private String textDescription;

    @Column(name = "htmlDescription")
    private String htmlDescription;

    @Column(name = "image")
    private String image;

    @OneToMany(targetEntity = Product.class)
    @JoinColumn(name = "productLine", referencedColumnName = "productLine")
//  @Fetch(FetchMode.JOIN)
    private Set<Product> productsList = new HashSet<>();
}
