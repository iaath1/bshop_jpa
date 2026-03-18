package com.bshop_jpa.demo.services;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bshop_jpa.demo.models.Order;
import com.bshop_jpa.demo.models.Status;

import jakarta.mail.internet.MimeMessage;

@Service
public class ConfirmationEmailService{
    
    @Autowired
    public JavaMailSender javaMailSender;

    public void sendConfirmationEmail(String toEmail, String code) {
        SimpleMailMessage confirmationEmail = new SimpleMailMessage();

        confirmationEmail.setFrom("iaathone@gmail.com");
        confirmationEmail.setTo(toEmail);
        confirmationEmail.setSubject("Account confirmation");
        confirmationEmail.setText("Your confirmation code: \n\n" + code + "\n\nInput it on web-site to activate your account.d");

        javaMailSender.send(confirmationEmail);
    }

    public void sendOrderInfoEmail(Status status, Order order) {
        SimpleMailMessage informationEmail = new SimpleMailMessage();
        String email;

        if(order.getGuestEmail() == null || order.getGuestEmail().isEmpty()) {
            email = order.getUser().getEmail();
        } else {
            email = order.getGuestEmail();
        }


        informationEmail.setFrom("iaathone@gmail.com");
        informationEmail.setTo(email);
        informationEmail.setSubject("Order information");
        
        switch (status.getName()) {
            case "PAID":
                informationEmail.setText("We have accepted your paid. If you have questions, please contact us on tel: ... ");
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
