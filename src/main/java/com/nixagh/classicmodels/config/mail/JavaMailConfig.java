package com.nixagh.classicmodels.config.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaMailConfig {
    private static final String MAIL_PROTOCOL = "smtp";
    private static final String MAIL_SMTP_AUTH = "true";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "true";
    private static final String MAIL_DEBUG = "true";
    private static final String MAIL_SMTP_HOST = "smtp.gmail.com";
    private static final int MAIL_SMTP_PORT = 587;
    private static final String MAIL_USERNAME = "nghiapro121d@gmail.com";
    private static final String MAIL_PASSWORD = "wsfydbobkwsjytng";
    public static final String MAIL_FROM = "nghiapro121d@gmail.com";

    @Bean
    public JavaMailSender javaMailSenderImpl() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        // set the host and port of the smtp server providing the email service
        javaMailSender.setHost(MAIL_SMTP_HOST);
        javaMailSender.setPort(MAIL_SMTP_PORT);

        // set your gmail username and password
        javaMailSender.setUsername(MAIL_USERNAME);
        javaMailSender.setPassword(MAIL_PASSWORD);

        // set properties to enable various properties
        javaMailSender.getJavaMailProperties().setProperty("mail.transport.protocol", MAIL_PROTOCOL);
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.auth", MAIL_SMTP_AUTH);
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", MAIL_SMTP_STARTTLS_ENABLE);
//        javaMailSender.getJavaMailProperties().setProperty("mail.debug", MAIL_DEBUG);

        return javaMailSender;
    }
}
