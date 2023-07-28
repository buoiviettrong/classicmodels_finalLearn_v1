package com.nixagh.classicmodels.service.customer_service;

import com.nixagh.classicmodels.dto._statistic.synthetic.overview.OverviewTop;
import com.nixagh.classicmodels.dto.statistical.request.CustomerStatisticDTO;
import com.nixagh.classicmodels.dto.statistical.request.StatisticalRequest;
import com.nixagh.classicmodels.dto.statistical.response.CustomerEachMonth;
import com.nixagh.classicmodels.dto.statistical.response.CustomerStatisticResponse;
import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.exception.exceptions.NotFoundEntity;
import com.nixagh.classicmodels.repository.customer.CustomerNoDSLRepository;
import com.nixagh.classicmodels.repository.customer.CustomerRepository;
import com.nixagh.classicmodels.repository.employer.EmployeeRepository;
import com.nixagh.classicmodels.utils.math.RoundUtil;
import com.nixagh.classicmodels.utils.page.PageUtil;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerNoDSLRepository customerNoDSLRepository;

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.getCustomers();
    }

    @Override
    public List<Customer> getCustomersBySalesRepEmployeeNumber(Long eNum) {
        Employee employee = employeeRepository.findByEmployeeNumber(eNum).orElseThrow(() -> new NotFoundEntity("Employee not found"));
        return customerRepository.getCustomersBySalesRepEmployeeNumber(employee);
    }

    @Override
    @Transactional
    public Long deleteCustomer(long customerNumber) {
        long index = customerRepository.deleteCustomer(customerNumber);
        if (index == 0) throw new NotFoundEntity("Customer not found");
        System.out.println("Customer deleted successfully " + customerNumber);
        return customerNumber;
    }

    @Override
    public CustomerStatisticResponse getCustomerStatistical(StatisticalRequest statisticalRequest) {
        CustomerStatisticResponse customerStatisticResponse = new CustomerStatisticResponse();
        DecimalFormat dfZero = new DecimalFormat("0.00");

        Date from = statisticalRequest.getFrom();
        Date to = statisticalRequest.getTo();

        if (from == null) {
            from = new Date(0);
        }
        if (to == null) {
            to = new Date();
        }

        long pageNumber = statisticalRequest.getPageInfo().getPageNumber();
        long pageSize = statisticalRequest.getPageInfo().getPageSize();

        List<CustomerStatisticDTO> result = customerRepository.getCustomerStatistical(from, to, pageNumber, pageSize)
                .stream()
                .map(tuple -> CustomerStatisticDTO.builder()
                        .customerName(tuple.get(0, String.class))
                        .customerNumber(tuple.get(1, Long.class))
                        .totalOrder(tuple.get(2, Integer.class))
                        .totalAmount(Double.parseDouble(dfZero.format(tuple.get(3, BigDecimal.class))))
                        .build()
                )
                .toList();
        Long total = customerRepository.countCustomerStatistical(from, to);

        customerStatisticResponse.setCustomers(result);
        customerStatisticResponse.setPageResponseInfo(PageUtil.getResponse(pageNumber, pageSize, total, (long) result.size()));
        return customerStatisticResponse;
    }

    @Override
    public CustomerEachMonth getCustomerEachMonth(String customerName, int year, int month, long pageNumber, long pageSize) {
        CustomerEachMonth customerEachMonth = new CustomerEachMonth();
        long offset = (pageNumber - 1) * pageSize;
        customerName = "%" + customerName + "%";
        List<CustomerStatisticDTO> customerStatisticDTOs = customerNoDSLRepository.getCustomerEachMonth(customerName, year, month, offset, pageSize)
                .stream()
                .map(tuple -> CustomerStatisticDTO.builder()
                        .customerNumber(Long.valueOf(tuple.get("customerNumber", Integer.class)))
                        .customerName(tuple.get("customerName", String.class))
                        .totalOrder(tuple.get("totalOrder", Long.class) != null ? tuple.get("totalOrder", Long.class).intValue() : 0)
                        .totalAmount(tuple.get("totalAmount", Double.class))
                        .build()
                )
                .toList();
        Long total = (long) customerNoDSLRepository.countCustomerEachMonth(customerName, year, month).size();

        customerEachMonth.setCustomers(customerStatisticDTOs);
        customerEachMonth.setPageResponseInfo(PageUtil.getResponse(pageNumber, pageSize, total, (long) customerStatisticDTOs.size()));
        return customerEachMonth;
    }

    @Override
    public OverviewTop.Customer getTop1Customer(Date from, Date to) {
        Tuple tuple = customerRepository.getTop1Customer(from, to);
        if (tuple == null)
            return OverviewTop.Customer.builder()
                    .CustomerNumber(-1L)
                    .customerName("No customer")
                    .quantityInvoice(0)
                    .totalMoney(0.0)
                    .build();
        return OverviewTop.Customer.builder()
                .CustomerNumber(tuple.get(0, Long.class))
                .customerName(tuple.get(1, String.class))
                .quantityInvoice(tuple.get(2, Integer.class))
                .totalMoney(RoundUtil.convert(tuple.get(3, Double.class), 2))
                .build();
    }
}
