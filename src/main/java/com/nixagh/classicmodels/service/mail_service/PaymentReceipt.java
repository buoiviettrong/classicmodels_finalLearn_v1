package com.nixagh.classicmodels.service.mail_service;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceipt {
    private String name;
    private String to;
    private String subject = "Classic Models - Payment Receipt";
    private String body;
    private List<ProductInfo> products = new ArrayList<>();

    public PaymentReceipt(String name, String to) {
        this.name = name;
        this.to = to;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductInfo {
        private String name;
        private Long quantity;
        private double price;
        private double total;
    }

    public double getTotal() {
        return products.stream().mapToDouble(ProductInfo::getTotal).sum();
    }

    public String getBody() {
        StringBuilder body = new StringBuilder();

        String headPayment = """
                <h2>Classic Models - Payment Receipt</h2>
                <p>Dear <b> :name </b>,</p>
                <p>Thank you for your payment.</p>
                <p>Here is your payment receipt:</p>
                <p><b>Product(s):</b></p>
                <table border="1" style="border-collapse: collapse; width: 100%;">
                    <thead>
                    <tr>
                        <th style="text-align: left; padding: 8px;">Name</th>
                        <th style="text-align: left; padding: 8px;">Quantity</th>
                        <th style="text-align: left; padding: 8px;">Price</th>
                        <th style="text-align: left; padding: 8px;">Total</th>
                    </tr>
                    </thead>
                """;

        StringBuilder bodyPayments = getBodyProductInfo();

        String footPayment = """
                </table>
                <p><b>Total:</b> %.2f </p>
                <p><b>Date:</b> %s </p>
                <p><b>Time:</b> %s </p>
                <p>Thank you for choosing Classic Models.</p>
                <p>Best regards,</p>
                <p>Classic Models</p>
                <p><i>This is an automated message, please do not reply.</i></p>
                """;
        body.append(headPayment.replace(":name", name));
        body.append(bodyPayments);
        body.append(footPayment.formatted(getTotal(), java.time.LocalDate.now(), java.time.LocalTime.now()));

        this.body = body.toString();
        return this.body;
    }

    private StringBuilder getBodyProductInfo() {
        StringBuilder bodyPayments = new StringBuilder();
        bodyPayments.append("<tbody>");

        for (ProductInfo product : products) {
            String bodyPayment = """
                    <tr>
                        <td style="text-align: left; padding: 8px;">%s</td>
                        <td style="text-align: left; padding: 8px;">%d</td>
                        <td style="text-align: left; padding: 8px;">%.2f</td>
                        <td style="text-align: left; padding: 8px;">%.2f</td>
                    </tr>
                    """.formatted(product.getName(), product.getQuantity(), product.getPrice(), product.getTotal());
            bodyPayments.append(bodyPayment);
        }

        bodyPayments.append("</tbody>");
        return bodyPayments;
    }
}


