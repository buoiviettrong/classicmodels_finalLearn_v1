package com.nixagh.classicmodels.service.mail_service;

import com.nixagh.classicmodels.config.mail.JavaMailConfig;
import com.nixagh.classicmodels.exception.exceptions.MailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements IMailService {
    PaymentReceipt paymentReceipt;
    private final JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendPaymentReceiptMail(String to) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject("Payment Receipt");
            helper.setFrom(JavaMailConfig.MAIL_FROM);
            helper.setTo(to);
            helper.setText(paymentReceipt.getBody(), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new MailSendingException("Mail sending failed");
        }
        return "Mail sent successfully";
    }

    public void setPaymentReceipt(PaymentReceipt paymentReceipt) {
        this.paymentReceipt = paymentReceipt;
    }

}
