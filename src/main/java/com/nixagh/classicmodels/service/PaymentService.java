package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.config.VnPayConfig;
import com.nixagh.classicmodels.controller.PaymentController;
import com.nixagh.classicmodels.entity.Payment;
import com.nixagh.classicmodels.repository.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public List<Payment> getAll() {
        return paymentRepository.getAll();
    }

    public List<Payment> getByDateRange(Date start, Date end) {
        return paymentRepository.getByPaymentDateBetween(start, end);
    }

    public List<Payment> getByCustomerNumber(Long customerNumber) {
        return paymentRepository.getByCustomerNumber(customerNumber);
    }

    public ResponseEntity<?> createPayment(HttpServletRequest request, PaymentController.CreatePaymentRequest payment) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
//        String orderType = req.getParameter("ordertype");
//        long amount = Integer.parseInt(req.getParameter("amount"))*100;
//        String bankCode = req.getParameter("bankCode");
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
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
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

    public ResponseEntity<?> vnPayReturn(Long vnpAmount,
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
    ) {
        if (!vnpResponseCode.equals("00")) {
            return ResponseEntity.ok("Thanh toán thất bại");
        }

        // cap nhat trang thai don hang
        String orderNumber = vnpTxnRef;

        return ResponseEntity.ok("Thanh toán thành công");
    }
}
