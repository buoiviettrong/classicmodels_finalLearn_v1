package com.nixagh.classicmodels.repository.product;

import com.nixagh.classicmodels.entity.Product;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = """
            SELECT  pl.productLine AS 'productLineCode',
                    t.totalSoldProduct AS 'totalSoldProduct',
                    t.totalMoney AS 'totalMoney'
            FROM product_lines pl
            LEFT JOIN (
            	SELECT  p.productLine,
            			SUM(od.quantityOrdered) AS 'totalSoldProduct',
            			SUM(od.priceEach * od.quantityOrdered) AS 'totalMoney'
            	FROM products p
            	LEFT JOIN order_details od ON od.productCode = p.productCode
            	LEFT JOIN orders o ON o.orderNumber = od.orderNumber
            	WHERE o.orderDate BETWEEN :from AND :to
            	    AND o.status = 'Shipped'
            	GROUP BY p.productLine
            	ORDER BY p.productLine ASC
            ) t ON t.productLine = pl.productLine
            """, nativeQuery = true)
    List<Tuple> getSyntheticProductLine(java.sql.Date from, java.sql.Date to);

    @Query(value = """
            SELECT  pl.productLine AS 'productLineCode',
                    t.totalSoldProduct AS 'totalSoldProduct',
                    t.totalMoney AS 'totalMoney'
            FROM product_lines pl
            LEFT JOIN (
            	SELECT  p.productLine,
            			SUM(od.quantityOrdered) AS 'totalSoldProduct',
            			SUM(od.priceEach * od.quantityOrdered) AS 'totalMoney'
            	FROM products p
            	LEFT JOIN order_details od ON od.productCode = p.productCode
            	LEFT JOIN orders o ON o.orderNumber = od.orderNumber
            	WHERE o.orderDate BETWEEN :from AND :to
            	    AND o.status = 'Shipped'
            	    AND LOWER(p.productLine) LIKE :typeProductLine
            	    AND (LOWER(p.productCode) LIKE :search OR LOWER(p.productName) LIKE :search)
            	GROUP BY p.productLine
            	ORDER BY p.productLine ASC
            ) t ON t.productLine = pl.productLine
            """, nativeQuery = true)
    List<Tuple> getSyntheticProductLine(@Param("from") java.sql.Date from,
                                        @Param("to") java.sql.Date to,
                                        @Param("typeProductLine") String typeProductLine,
                                        @Param("search") String search);

    @Query(value = """
            SELECT  p.productLine AS 'productLineCode',
                    p.productCode AS 'productCode',
                    p.productName AS 'productName',
                    SUM(od.quantityOrdered) AS 'totalSold',
                    SUM(od.priceEach * od.quantityOrdered) AS 'totalMoney' FROM products p
            JOIN order_details od ON od.productCode = p.productCode
            JOIN orders o ON o.orderNumber = od.orderNumber
            WHERE o.orderDate >= DATE(:from) AND o.orderDate <= DATE(:to)
                AND o.status = 'Shipped' AND LOWER(p.productLine) LIKE :typeProductLine
                AND (LOWER(p.productCode) LIKE :search OR LOWER(p.productName) LIKE :search)
            GROUP BY p.productLine,
                    p.productName,
                    p.productCode
            ORDER BY totalMoney DESC
            LIMIT :offset, :pageSize
            """, nativeQuery = true, name = "getDetailStatisticDetail")
    List<Tuple> getDetailStatisticDetail(@Param("from") java.sql.Date from, @Param("to") java.sql.Date to, String typeProductLine, String search, int offset, int pageSize);


    @Query(value = """
            SELECT  p.productLine AS 'productLineCode',
                    SUM(od.quantityOrdered) AS 'totalSold',
                    SUM(od.priceEach * od.quantityOrdered) AS 'totalMoney'
            FROM products p
            JOIN order_details od ON od.productCode = p.productCode
            JOIN orders o ON o.orderNumber = od.orderNumber
            WHERE o.orderDate >= :from AND o.orderDate <= :to
                AND  o.status = 'Shipped'
                AND LOWER(p.productLine) LIKE :typeProductLine
                AND (LOWER(p.productCode) LIKE :search OR LOWER(p.productName) LIKE :search)
            GROUP BY p.productLine
            """, nativeQuery = true)
    List<Tuple> getTotalSoldProductAndProfit(@Param("from") java.sql.Date from, @Param("to") java.sql.Date to, String typeProductLine, String search);

//    @Query(value = """
//            SELECT  p.productLine AS 'productLineCode',
//                    p.productCode AS 'productCode',
//                    p.productName AS 'productName',
//                    SUM(od.quantityOrdered) AS 'totalSold',
//                    SUM(od.priceEach * od.quantityOrdered) AS 'totalMoney'
//            FROM products p
//            JOIN order_details od ON od.productCode = p.productCode
//            JOIN orders o ON o.orderNumber = od.orderNumber
//            WHERE o.orderDate >= :from AND o.orderDate <= :to
//                AND o.status = 'Shipped'
//                AND LOWER(p.productLine) LIKE :typeProductLine
//                AND (LOWER(p.productCode) LIKE :search OR LOWER(p.productName) LIKE :search)
//            GROUP BY    p.productLine,
//                        p.productName,
//                        p.productCode
//            ORDER BY totalMoney DESC
//            LIMIT :offset, :pageSize
//            """, nativeQuery = true, name = "getDetailStatisticDetail")
//    List<Tuple> getDetailStatisticDetail(@Param("from") java.sql.Date from, @Param("to") java.sql.Date to, String typeProductLine, String search, int offset, int pageSize);
}
