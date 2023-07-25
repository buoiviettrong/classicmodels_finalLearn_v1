package com.nixagh.classicmodels.service.customer_service;

import com.nixagh.classicmodels.dto._statistic.overview.OverviewTop;
import com.nixagh.classicmodels.dto.statistical.request.StatisticalRequest;
import com.nixagh.classicmodels.dto.statistical.response.CustomerEachMonth;
import com.nixagh.classicmodels.dto.statistical.response.CustomerStatisticResponse;
import com.nixagh.classicmodels.entity.Customer;

import java.util.List;

public interface ICustomerService {
    List<Customer> getCustomers();

    List<Customer> getCustomersBySalesRepEmployeeNumber(Long eNum);

    public Long deleteCustomer(long customerNumber);

    CustomerStatisticResponse getCustomerStatistical(StatisticalRequest statisticalRequest);

    CustomerEachMonth getCustomerEachMonth(String customerName, int year, int month, long pageNumber, long pageSize);

    OverviewTop.Customer getTop1Customer(String from, String to);
}
