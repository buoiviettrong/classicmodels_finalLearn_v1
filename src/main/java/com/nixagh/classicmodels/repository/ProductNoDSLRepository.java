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
                    ROUND(SUM(od.quantityOrdered * od.priceEach), 2) AS totalAmount
            FROM order_details od
            JOIN products p ON od.productCode = p.productCode
            JOIN orders o on o.orderNumber = od.orderNumber
            WHERE o.orderDate BETWEEN :from AND :to
                 AND o.status = 'Shipped'
            GROUP BY p.productCode
            ORDER BY totalAmount DESC
            LIMIT :offset, :pageSize
            """
            , nativeQuery = true)
    List<Tuple> getProductStatistical(Date from, Date to, long offset, long pageSize);


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

    @Query(value = """
            SELECT COUNT(DISTINCT p.productCode)
            FROM order_details od
            JOIN products p ON od.productCode = p.productCode
            JOIN orders o on o.orderNumber = od.orderNumber
            WHERE o.orderDate BETWEEN :from AND :to
                AND o.status = 'Shipped'
            """
            , nativeQuery = true)
    long countProductStatistical(Date from, Date to);

    @Query(value = """
            SELECT
                p.productCode,
                p.productName,
                t.totalSoldQuantity,
                t.totalAmount,
                p.buyPrice,
                ROUND(t.totalAmount - p.buyPrice * t.totalSoldQuantity, 2) as totalProfit,
                t.priceEach as soldPrice
            FROM products p
            LEFT JOIN (
                SELECT
                    od.productCode,
                    SUM(od.quantityOrdered) as totalSoldQuantity,
                    ROUND(SUM(od.priceEach*od.quantityOrdered), 2) as totalAmount,
                    od.priceEach
                FROM order_details od
                JOIN orders o on o.orderNumber = od.orderNumber
                WHERE year(o.orderDate) = :year AND month(o.orderDate) = :month
                GROUP BY od.productCode
            ) t on t.productCode = p.productCode
            ORDER BY t.totalAmount DESC
            LIMIT :offset, :pageSize
            """, nativeQuery = true)
    List<Tuple> getProductEachMonth(int year, int month, long offset, long pageSize);

    @Query(value = """
            SELECT p.productCode, p.productName, t.totalSoldQuantity, t.totalAmount, p.buyPrice
            FROM products p
            LEFT JOIN (
                SELECT
                    od.productCode,
                    SUM(od.quantityOrdered) as totalSoldQuantity,
                    ROUND(SUM(od.priceEach*od.quantityOrdered), 2) as totalAmount
                FROM order_details od
                JOIN orders o on o.orderNumber = od.orderNumber
                WHERE year(o.orderDate) = :year AND month(o.orderDate) = :month
                GROUP BY od.productCode
            ) t on t.productCode = p.productCode
            ORDER BY t.totalAmount DESC
            """, nativeQuery = true)
    List<Tuple> countProductEachMonth(int year, int month);

    @Query(value = """
            SELECT
                p.productCode,
                p.productName,
                t.totalSoldQuantity,
                t.totalAmount,
                p.buyPrice,
                ROUND(t.totalAmount - p.buyPrice * t.totalSoldQuantity, 2) as totalProfit,
                t.priceEach as soldPrice
            FROM products p
            LEFT JOIN (
                SELECT
                    od.productCode,
                    SUM(od.quantityOrdered) as totalSoldQuantity,
                    ROUND(SUM(od.priceEach*od.quantityOrdered), 2) as totalAmount,
                    od.priceEach
                FROM order_details od
                JOIN orders o on o.orderNumber = od.orderNumber
                WHERE year(o.orderDate) = :year AND month(o.orderDate) = :month
                GROUP BY od.productCode
            ) t on t.productCode = p.productCode
            ORDER BY t.totalAmount DESC
            """, nativeQuery = true)
    List<Tuple> getExportProduct(int year, int month);
}
