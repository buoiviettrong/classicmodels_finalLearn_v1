package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Order;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

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

    @Query(value = """
            SELECT o.status as status, count(o.orderNumber) as count, round(sum(t.totalProfit), 2) as profit
            FROM orders o
            join (  select o.orderNumber as orderNumber, sum(od.quantityOrdered * od.priceEach) as totalProfit
                    from order_details od
                    join orders o on od.orderNumber = o.orderNumber
                    where o.orderDate between :from and :to
                    group by o.orderNumber
                 ) as t on o.orderNumber = t.orderNumber
            WHERE o.orderDate between :from and :to
            GROUP BY o.status
            """, nativeQuery = true)
    List<Tuple> getOrderStatusStatistical(Date from, Date to);

    @Query(value = """
            SELECT month(o.orderDate) as month, o.status, t.profit as profit
            FROM classicmodels.orders o
            LEFT JOIN (
                       SELECT round(sum(od.quantityOrdered * od.priceEach), 2) as profit, od.orderNumber as orderNumber
                       FROM order_details od
                       GROUP BY od.orderNumber
            ) t ON o.orderNumber = t.orderNumber
            WHERE year(o.orderDate) = :year
            """, nativeQuery = true)
    List<Tuple> getOrderEachMonth(int year);

    @Query(value = """
            SELECT o.orderNumber as orderNumber,
                o.orderDate as orderDate,
                round(sum(od.quantityOrdered * od.priceEach), 2) as totalAmount,
                o.status as status
            FROM orders o
            LEFT JOIN order_details od ON o.orderNumber = od.orderNumber
            WHERE o.customerNumber = :customerNumber
            GROUP BY o.orderNumber
            ORDER BY o.orderDate DESC
            """, nativeQuery = true)
    List<Tuple> findOrderByCustomerNumber(Long customerNumber);

    Optional<Object> findByOrderNumber(Long orderNumber);
}




