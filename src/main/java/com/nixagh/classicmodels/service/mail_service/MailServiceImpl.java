package com.nixagh.classicmodels.service.mail_service;

import com.nixagh.classicmodels.config.mail.JavaMailConfig;
import com.nixagh.classicmodels.exception.exceptions.MailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class MailServiceImpl implements IMailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendPaymentReceiptMail(PaymentReceipt paymentReceipt) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(JavaMailConfig.MAIL_FROM);
            helper.setTo(paymentReceipt.getTo());
            helper.setSubject(paymentReceipt.getSubject());
            helper.setText(paymentReceipt.getBody(), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new MailSendingException("Mail sending failed");
        }
    }

}
