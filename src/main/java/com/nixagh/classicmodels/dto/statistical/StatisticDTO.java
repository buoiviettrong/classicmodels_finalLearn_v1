package com.nixagh.classicmodels.dto.statistical;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDTO {
    private int year;
    private double profitOfTheYear;
    private List<Month> months = new ArrayList<>();

    public record Month(int month, double profitOfTheMonth, List<Customer> customers) {
    }

    public record Customer(Long customerNumber, String customerName, double profitOfTheCustomer,
                           List<Product_> products) {
    }

    public record Product_(String productCode, String productName, int quantity, double profit) {
    }
}
