package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Customer;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerNoDSLRepository extends JpaRepository<Customer, Long> {
    @Query(value = """
            SELECT distinct c.customerNumber, c.customerName, t.totalOrder, t.totalAmount
            FROM customers c
            LEFT JOIN (
                SELECT o.customerNumber as customerNumber, count(customerNumber) as totalOrder, round(sum(od.priceEach * od.quantityOrdered), 2) as totalAmount
                FROM orders o
                LEFT JOIN order_details od on o.orderNumber = od.orderNumber
                WHERE YEAR(o.orderDate) = :year
                AND MONTH(o.orderDate) = :month
                AND o.status = 'Shipped'
                GROUP BY customerNumber
            ) as t on t.customerNumber = c.customerNumber
            ORDER BY t.totalAmount DESC
            LIMIT :offset, :pageSize
            """, nativeQuery = true)
    List<Tuple> getCustomerEachMonth(int year, int month, long offset, long pageSize);

    @Query(value = """
            SELECT distinct c.customerNumber, c.customerName, t.totalOrder, t.totalAmount
            FROM customers c
            LEFT JOIN (
                SELECT o.customerNumber as customerNumber, count(customerNumber) as totalOrder, round(sum(od.priceEach * od.quantityOrdered), 2) as totalAmount
                FROM orders o
                LEFT JOIN order_details od on o.orderNumber = od.orderNumber
                WHERE YEAR(o.orderDate) = :year
                AND MONTH(o.orderDate) = :month
                AND o.status = 'Shipped'
                GROUP BY customerNumber
            ) as t on t.customerNumber = c.customerNumber
            ORDER BY t.totalAmount DESC
            """, nativeQuery = true)
    List<Tuple> countCustomerEachMonth(int year, int month);
}
