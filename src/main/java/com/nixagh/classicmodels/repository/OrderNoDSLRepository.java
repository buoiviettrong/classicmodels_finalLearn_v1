package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Order;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface OrderNoDSLRepository extends JpaRepository<Order, Long> {

    @Query(value = """
            SELECT o.* , ROUND(SUM(od.quantityOrdered * od.priceEach), 2) as 'totalProfit'
            FROM orders o
            LEFT JOIN order_details od ON o.orderNumber = od.orderNumber
            WHERE o.orderDate between :from and :to
            AND o.status = 'Shipped'
            GROUP BY o.orderNumber
            """, nativeQuery = true)
    List<Tuple> getOrderByTimeRange(@Param("from") Date from,
                                    @Param("to") Date to);

    @Query(value = """
            SELECT month(o.orderDate) as month,
                round(sum(od.priceEach * od.quantityOrdered), 2) as profit
            FROM orders o
            LEFT JOIN order_details od on o.orderNumber = od.orderNumber
            WHERE year(o.orderDate) = :year
            AND o.status = 'Shipped'
            GROUP BY month(o.orderDate)
            """, nativeQuery = true)
    List<Tuple> getProfitEachMonthInYear(int year);
}




