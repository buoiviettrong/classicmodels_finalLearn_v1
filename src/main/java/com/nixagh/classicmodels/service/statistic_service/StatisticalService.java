package com.nixagh.classicmodels.service.statistic_service;

import com.nixagh.classicmodels.controller.StatisticalController;
import com.nixagh.classicmodels.dto._statistic.Details.*;
import com.nixagh.classicmodels.dto._statistic.Synthetic.SyntheticStatisticRequest;
import com.nixagh.classicmodels.dto._statistic.Synthetic.SyntheticStatisticResponse;
import com.nixagh.classicmodels.dto._statistic.Synthetic.details.SyntheticProduct;
import com.nixagh.classicmodels.dto._statistic.Synthetic.overview.Overview;
import com.nixagh.classicmodels.dto._statistic.Synthetic.overview.OverviewTop;
import com.nixagh.classicmodels.dto._statistic.Synthetic.overview.OverviewTotal;
import com.nixagh.classicmodels.dto.date.DateRange;
import com.nixagh.classicmodels.dto.statistical.request.*;
import com.nixagh.classicmodels.dto.statistical.response.*;
import com.nixagh.classicmodels.service.customer_service.ICustomerService;
import com.nixagh.classicmodels.service.order_service.IOrderService;
import com.nixagh.classicmodels.service.product_service.IProductService;
import com.nixagh.classicmodels.utils.math.RoundUtil;
import com.nixagh.classicmodels.utils.page.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticalService implements IStatisticalService {

    private final IOrderService orderService;
    private final IProductService productService;
    private final ICustomerService customerService;

    @Override
    public StatisticalResponse getStatistical(StatisticalRequest statisticalRequest) {
        StatisticalResponse response = new StatisticalResponse();
        // set time range
        response.setTimeRange(new DateRange(statisticalRequest.getFrom(), statisticalRequest.getTo()));

        // get page infos from request
        long pageSize = statisticalRequest.getPageInfo().getPageSize() == 0 ? 10 : statisticalRequest.getPageInfo().getPageSize();
        long pageNumber = statisticalRequest.getPageInfo().getPageNumber() == 0 ? 1 : statisticalRequest.getPageInfo().getPageNumber();

        // get orders by time range and pagination
        List<OrderWithProfit> orders = orderService.getOrderByTimeRange(statisticalRequest.getFrom(), statisticalRequest.getTo());
        List<OrderWithProfit> order_ = orders.stream().skip(pageSize * (pageNumber - 1)).limit(pageSize).toList();

        // create order statistic info
        OrderStatistic orderStatistic = new OrderStatistic();
        orderStatistic.setTotalOrder((long) orders.size());
        orderStatistic.setOrders(order_);
        orderStatistic.setPageResponseInfo(PageUtil.getResponse(pageNumber, pageSize, (long) orders.size(), (long) order_.size()));

        // set response
        response.setOrder(orderStatistic);
        response.setTotalProfit(orders.stream().map(OrderWithProfit::getProfit).reduce(0.00, Double::sum));
        return response;
    }

    @Override
    public List<Top10ProductResponse> getTop10Products(Date from, Date to) {
        return productService.getTop10Products(from, to);
    }

    @Override
    public Map<Integer, Double> getProfitEachMonthInYear(int year) {
        return orderService.getProfitEachMonthInYear(year);
    }

    @Override
    public Map<Integer, ProductsEachMonthInYear> getProductsEachMonthInYear(int year) {
        return productService.getProductsEachMonthInYear(year);
    }

    @Override
    public StatisticDTO getAllStatistical() {
        return productService.getAllStatistical();
    }

    @Override
    public CustomerStatisticResponse getCustomerStatistical(StatisticalRequest statisticalRequest) {
        return customerService.getCustomerStatistical(statisticalRequest);
    }

    @Override
    public ProductStatisticResponse getProductStatistical(StatisticalRequest statisticalRequest) {
        return productService.getProductStatistical(statisticalRequest);
    }

    @Override
    public OrderStatisticResponse getOrderStatistical(StatisticalRequest statisticalRequest) {
        return orderService.getOrderStatistical(statisticalRequest);
    }

    @Override
    public List<OrderStatusStatisticResponse> getOrderStatusStatistical(StatisticalRequest statisticalRequest) {
        return orderService.getOrderStatusStatistical(statisticalRequest);
    }

    @Override
    public List<OrderEachMonth> getOrderEachMonth(int year) {
        return orderService.getOrderEachMonth(year);
    }

    @Override
    public ProductEachMonth getProductEachMonth(int year, int month, int pageNumber, int pageSize) {
        return productService.getProductEachMonth(year, month, pageNumber, pageSize);
    }

    @Override
    public CustomerEachMonth getCustomerEachMonth(String customerName, int year, int month, int pageNumber, int pageSize) {
        return customerService.getCustomerEachMonth(customerName, year, month, pageNumber, pageSize);
    }

    @Override
    public StatisticalController.ByteArrayInputStreamResponse getExportProduct(int year, int month)
            throws IOException, NoSuchFieldException, IllegalAccessException {
        return productService.getExportProduct(year, month);
    }

    @Override
    public SyntheticStatisticResponse getSyntheticStatistic(SyntheticStatisticRequest syntheticStatisticRequest) {
        SyntheticStatisticResponse response = new SyntheticStatisticResponse();

        var from_ = syntheticStatisticRequest.getFrom();
        var to_ = syntheticStatisticRequest.getTo();
//        var type = syntheticStatisticRequest.getType();

        var from = new java.sql.Date(from_.getTime());
        var to = new java.sql.Date(to_.getTime());

        // create overview
        Overview overview = new Overview();
        // get overview total
        // get total order
        Long totalOrder = orderService.getTotalOrder(from, to) == null ? 0 : orderService.getTotalOrder(from, to);
        // get total sold product and total profit
        OverviewTotal totalSoldProductAndProfit = productService.getTotalSoldProductAndProfit(from, to);
        totalSoldProductAndProfit.setTotalMoney(totalSoldProductAndProfit.getTotalMoney() == null ? 0 : totalSoldProductAndProfit.getTotalMoney());
        totalSoldProductAndProfit.setTotalSoldProduct(totalSoldProductAndProfit.getTotalSoldProduct() == null ? 0 : totalSoldProductAndProfit.getTotalSoldProduct());

        totalSoldProductAndProfit.setTotalInvoice(totalOrder);

        // get overview top
        // get top order by profit
        OverviewTop.Invoice top1Order = orderService.getTop1Order(from, to);
        // get top product by quantity
        OverviewTop.Product top1Product = productService.getTop1Product(from, to);
        // get top product line by quantity
        OverviewTop.ProductLine top1ProductLine = productService.getTop1ProductLine(from, to);
        // get top customer by quantity order
        OverviewTop.Customer top1Customer = customerService.getTop1Customer(from, to);

        // set overview top
        overview.setOverviewTop(new OverviewTop(top1Order, top1Product, top1ProductLine, top1Customer));
        // set overview total
        overview.setOverviewTotal(totalSoldProductAndProfit);

        // create new product statistic
        SyntheticProduct syntheticProduct = new SyntheticProduct();
        // get total product
        Long totalProduct = totalSoldProductAndProfit.getTotalSoldProduct();
        // get synthetic product line
        List<SyntheticProduct.SyntheticProductLine> syntheticProductLine = productService.getSyntheticProductLine(from, to);
        // set synthetic product
        syntheticProduct.setTotalProduct(totalProduct);
        syntheticProduct.setSyntheticProductLine(syntheticProductLine);

        response.setSyntheticProduct(syntheticProduct);
        response.setOverview(overview);
        return response;
    }

    @Override
    public DetailsStatisticResponse getDetailStatisticDetail(DetailsStatisticRequest detailsStatisticRequest, Long pageNumber, Long pageSize) {

        var PRODUCT_LINE_ALL = "0";

        // get request info
        var from = detailsStatisticRequest.getFrom();
        var to = detailsStatisticRequest.getTo();
        var typeProductLine = detailsStatisticRequest.getTypeProductLine();
        var search = detailsStatisticRequest.getSearch();

        // set default value
        if (pageNumber == null || pageNumber == 0) {
            pageNumber = 1L;
        }

        if (pageSize == null || pageSize == 0) {
            pageSize = 10L;
        }

        if (search == null || search.equals("null") || search.equals("undefined")) {
            search = "";
        }

        var offset = pageSize * (pageNumber - 1);

        if (PRODUCT_LINE_ALL.equalsIgnoreCase(typeProductLine)) {
            typeProductLine = "";
        }

        // convert date
        java.sql.Date sqlFrom = new java.sql.Date(from.getTime());
        java.sql.Date sqlTo = new java.sql.Date(to.getTime());

        // create response
        DetailsStatisticResponse response = new DetailsStatisticResponse();
        // create overview
        DetailsOverview overview = productService.getTotalSoldProductAndProfit(sqlFrom, sqlTo, typeProductLine, search);
        // create table
        DetailsTable table = new DetailsTable();
        List<DetailsProduct> products = productService.getDetailStatisticDetail(sqlFrom, sqlTo, typeProductLine, search, offset, pageSize);
        Long totalProduct = productService.countDetailStatisticDetail(sqlFrom, sqlTo, typeProductLine, search, offset, pageSize);
        // set table
        table.setProducts(products);
        table.setTotalQuantity(products.stream().map(DetailsProduct::getQuantitySold).reduce(0L, Long::sum));
        table.setTotalMoney(RoundUtil.convert(products.stream().map(DetailsProduct::getTotalMoney).reduce(0.00, Double::sum), 2));
        table.setPageResponseInfo(PageUtil.getResponse(pageNumber, pageSize, totalProduct, (long) products.size()));

        // set overview
        if ("".equalsIgnoreCase(typeProductLine)) {
            overview.setProductLineCode("All");
        }
        // set response
        response.setOverview(overview);
        response.setTable(table);
        // return response
        return response;
    }
}
