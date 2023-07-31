package com.nixagh.classicmodels.service.payment_service;

import com.nixagh.classicmodels.config.vn_pay.VnPayConfig;
import com.nixagh.classicmodels.controller.PaymentController;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.entity.OrderDetail;
import com.nixagh.classicmodels.entity.Payment;
import com.nixagh.classicmodels.entity.enums.PaymentStatus;
import com.nixagh.classicmodels.repository.order.OrderRepository;
import com.nixagh.classicmodels.repository.payment.PaymentRepository;
import com.nixagh.classicmodels.service.mail_service.IMailService;
import com.nixagh.classicmodels.service.mail_service.PaymentReceipt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final IMailService mailService;

    @Override
    public List<Payment> getAll() {
        return paymentRepository.getAll();
    }

    @Override
    public List<Payment> getByDateRange(Date start, Date end) {
        return paymentRepository.getByPaymentDateBetween(start, end);
    }

    @Override
    public List<Payment> getByCustomerNumber(Long customerNumber) {
        return paymentRepository.getByCustomerNumber(customerNumber);
    }

    @Override
    public ResponseEntity<?> createPayment(HttpServletRequest request, PaymentController.CreatePaymentRequest payment) {
        int amount = (int) (payment.amount() * 100);

        String vnp_TxnRef = payment.orderNumber();
        String vnp_IpAddr = VnPayConfig.getIpAddress(request);
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", VnPayConfig.vnp_CurrCode);

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", VnPayConfig.vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnURL);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayURL + "?" + queryUrl;
        @Getter
        @Setter
        class PaymentResponse implements Serializable {
            private String status;
            private String message;
            private String paymentUrl;

            public PaymentResponse(String paymentUrl) {
                this.status = "Ok";
                this.message = "Create payment success";
                this.paymentUrl = paymentUrl;
            }
        }

        return ResponseEntity.ok(new PaymentResponse(paymentUrl));
    }

    @Override
    @Transactional
    public void vnPayReturn(
            HttpServletResponse response,
            Long vnpAmount,
            String vnpBankCode,
            String vnpBankTranNo,
            String vnpCardType,
            String vnpOrderInfo,
            String vnpPayDate,
            String vnpResponseCode,
            String vnpTmnCode,
            String vnpTransactionNo,
            String vnpTxnRef,
            String vnpSecureHash,
            String vnpTransactionStatus
    ) throws ParseException, IOException {
        var redirectUrl = "http://localhost:8081/manager/order-history";
        if (!"00".equals(vnpResponseCode)) {
            response.sendRedirect(redirectUrl);
        }
        // check hash data
        // check order status
        // check amount
        // check transaction status

        // update order status
        Long orderNumber = Long.valueOf(vnpTxnRef);

        Date paymentDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(vnpPayDate);

        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        order.setPaymentStatus(PaymentStatus.PAID.getPaymentStatus());
        order.setPaymentDate(paymentDate);
        orderRepository.save(order);

        // create payment mail receipt
        PaymentReceipt paymentReceipt = new PaymentReceipt(order.getCustomer().getCustomerName(), order.getCustomer().getEmail());

        List<OrderDetail> orderDetails = order.getOrderDetail();
        paymentReceipt.setProducts(orderDetails.stream().map(orderDetail -> PaymentReceipt.ProductInfo.builder()
                .name(orderDetail.getProduct().getProductName())
                .price(orderDetail.getPriceEach())
                .quantity(orderDetail.getQuantityOrdered())
                .total(orderDetail.getPriceEach() * orderDetail.getQuantityOrdered())
                .build()).toList());

        // send mail
        mailService.sendPaymentReceiptMail(paymentReceipt);

        response.sendRedirect(redirectUrl);
    }
}
