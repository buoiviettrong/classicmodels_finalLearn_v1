package com.nixagh.classicmodels.dto._statistic.overview;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OverviewTop {
    private Invoice invoice;
    private Product product;
    private ProductLine productLine;
    private Customer customer;

    @Getter
    @Setter
    @Builder
    public static class Invoice {
        private Integer orderNumber;
        private String orderDate;
        private Double totalMoney;
    }

    @Getter
    @Setter
    @Builder
    public static class Product {
        private String productCode;
        private String productName;
        private Integer quantity;
    }

    @Getter
    @Setter
    @Builder
    public static class ProductLine {
        private String ProductLineCode;
        private Integer quantity;
    }

    @Getter
    @Setter
    @Builder
    public static class Customer {
        private Integer CustomerNumber;
        private String customerName;
        private Integer quantityInvoice;
        private Double totalMoney;
    }
}
