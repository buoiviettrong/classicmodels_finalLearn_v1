package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.dto.statistical.request.CustomerStatisticDTO;
import com.nixagh.classicmodels.dto.statistical.request.StatisticalRequest;
import com.nixagh.classicmodels.dto.statistical.response.CustomerEachMonth;
import com.nixagh.classicmodels.dto.statistical.response.CustomerStatisticResponse;
import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.exception.NotFoundEntity;
import com.nixagh.classicmodels.repository.CustomerNoDSLRepository;
import com.nixagh.classicmodels.repository.CustomerRepository;
import com.nixagh.classicmodels.repository.EmployeeRepository;
import com.nixagh.classicmodels.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerNoDSLRepository customerNoDSLRepository;

    public List<Customer> getCustomers() {
        return customerRepository.getCustomers();
    }


    public List<Customer> getCustomersBySalesRepEmployeeNumber(Long eNum) throws IllegalAccessException {
        Employee employee = employeeRepository.findByEmployeeNumber(eNum).orElseThrow(() -> new NotFoundEntity("Employee not found"));
        return customerRepository.getCustomersBySalesRepEmployeeNumber(employee);
    }

    @Transactional
    public Long deleteCustomer(long customerNumber) {
        long index = customerRepository.deleteCustomer(customerNumber);
        System.out.println("Customer deleted successfully " + customerNumber);
        return customerNumber;
    }

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

    public CustomerEachMonth getCustomerEachMonth(int year, int month, long pageNumber, long pageSize) {
        CustomerEachMonth customerEachMonth = new CustomerEachMonth();
        long offset = (pageNumber - 1) * pageSize;

        List<CustomerStatisticDTO> customerStatisticDTOs = customerNoDSLRepository.getCustomerEachMonth(year, month, offset, pageSize)
                .stream()
                .map(tuple -> CustomerStatisticDTO.builder()
                        .customerNumber(Long.valueOf(tuple.get("customerNumber", Integer.class)))
                        .customerName(tuple.get("customerName", String.class))
                        .totalOrder(tuple.get("totalOrder", Integer.class))
                        .totalAmount(tuple.get("totalAmount", Double.class))
                        .build()
                )
                .toList();
        Long total = (long) customerNoDSLRepository.countCustomerEachMonth(year, month).size();

        customerEachMonth.setCustomers(customerStatisticDTOs);
        customerEachMonth.setPageResponseInfo(PageUtil.getResponse(pageNumber, pageSize, total, (long) customerStatisticDTOs.size()));
        return customerEachMonth;
    }
}
