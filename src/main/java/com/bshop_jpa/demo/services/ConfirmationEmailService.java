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

    @Async
    public void sendConfirmationEmail(String toEmail, String code) {
        SimpleMailMessage confirmationEmail = new SimpleMailMessage();

        confirmationEmail.setFrom(mailFrom);
        confirmationEmail.setTo(toEmail);
        confirmationEmail.setSubject("Account confirmation");
        confirmationEmail.setText(
                "Your confirmation code: \n\n" + code + "\n\nEnter it on the website to activate your account.");

        javaMailSender.send(confirmationEmail);
    }

    @Async
    public void sendOrderInfoEmail(Status status, Order order) {
        SimpleMailMessage informationEmail = new SimpleMailMessage();
        String email;

        if (order.getGuestEmail() == null || order.getGuestEmail().isEmpty()) {
            email = order.getUser().getEmail();
        } else {
            email = order.getGuestEmail();
        }

        informationEmail.setFrom(mailFrom);
        informationEmail.setTo(email);
        informationEmail.setSubject("Order information");

        switch (status.getName()) {
            case "PAID":
                informationEmail
                        .setText("We have accepted your paid. If you have questions, please contact us on tel: ... ");
                break;
            case "SHIPPING":
                informationEmail.setText("Your order have been delivered to Delivery Company.");
                break;
            default:
                break;
        }

        javaMailSender.send(informationEmail);
    }

}
