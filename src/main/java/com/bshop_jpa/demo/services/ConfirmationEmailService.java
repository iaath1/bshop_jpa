package com.bshop_jpa.demo.services;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

}
