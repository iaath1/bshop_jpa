package com.bshop_jpa.demo.services;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Order;
import com.bshop_jpa.demo.models.Status;

import jakarta.mail.internet.MimeMessage;

@Service
public class ConfirmationEmailService {

    @Autowired
    public JavaMailSender javaMailSender;

    @Value("${MAIL_USERNAME}")
    private String mailFrom;

    // Добавь логгер
    private static final org.slf4j.Logger logger = 
        org.slf4j.LoggerFactory.getLogger(ConfirmationEmailService.class);

    @Async
    public void sendConfirmationEmail(String toEmail, String code) {
    try {
        System.out.println("=== SENDING EMAIL TO: " + toEmail);
        System.out.println("=== MAIL FROM: " + mailFrom);

        SimpleMailMessage confirmationEmail = new SimpleMailMessage();
        confirmationEmail.setFrom(mailFrom);
        confirmationEmail.setTo(toEmail);
        confirmationEmail.setSubject("Account confirmation");
        confirmationEmail.setText(
            "Your confirmation code: \n\n" + code +
            "\n\nEnter it on the website to activate your account.");

        javaMailSender.send(confirmationEmail);
        System.out.println("=== EMAIL SENT SUCCESSFULLY TO: " + toEmail);

    } catch (Exception e) {
        System.out.println("=== EMAIL ERROR: " + e.getMessage());
        e.printStackTrace();
    }
}

    @Async
    public void sendOrderInfoEmail(Status status, Order order) {
        try {
            SimpleMailMessage informationEmail = new SimpleMailMessage();
            String email = (order.getGuestEmail() == null || order.getGuestEmail().isEmpty())
                ? order.getUser().getEmail()
                : order.getGuestEmail();

            informationEmail.setFrom(mailFrom);
            informationEmail.setTo(email);
            informationEmail.setSubject("Order information");

            switch (status.getName()) {
                case "PAID":
                    informationEmail.setText("We have accepted your paid. If you have questions, please contact us on tel: ...");
                    break;
                case "SHIPPING":
                    informationEmail.setText("Your order have been delivered to Delivery Company.");
                    break;
                default:
                    break;
            }

            javaMailSender.send(informationEmail);
            logger.info("Order email sent to: {}", email);

        } catch (Exception e) {
            logger.error("Failed to send order email: {}", e.getMessage(), e);
        }
    }
}