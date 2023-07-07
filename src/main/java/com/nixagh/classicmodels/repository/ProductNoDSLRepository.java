package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Product;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductNoDSLRepository extends JpaRepository<Product, String> {

    @Query(value = """
            SELECT  p.productCode AS productCode,
                    p.productName AS productName,
                    SUM(od.quantityOrdered) AS totalSoldQuantity,
                    ROUND(SUM(od.quantityOrdered * od.priceEach), 2) AS totalProfit
            FROM order_details od
            JOIN products p ON od.productCode = p.productCode
            JOIN orders o on o.orderNumber = od.orderNumber
            WHERE o.orderDate BETWEEN :from AND :to
            AND o.status = 'Shipped'
            GROUP BY p.productCode
            ORDER BY totalProfit DESC
            LIMIT 10
            """
            , nativeQuery = true)
    List<Tuple> getTop10Products(Date from, Date to);
}
