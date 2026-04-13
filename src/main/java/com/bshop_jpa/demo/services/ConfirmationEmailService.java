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

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey;

    @Value("${MAIL_USERNAME}")
    private String mailFrom;

    // Добавь логгер
    private final org.springframework.web.reactive.function.client.WebClient webClient =
        org.springframework.web.reactive.function.client.WebClient.create("https://api.brevo.com");

    @Async
    public void sendConfirmationEmail(String toEmail, String code) {
        try {
            System.out.println("=== SENDING EMAIL TO: " + toEmail);

            String body = """
                {
                    "sender": {"email": "%s"},
                    "to": [{"email": "%s"}],
                    "subject": "Account confirmation",
                    "textContent": "Your confirmation code:\\n\\n%s\\n\\nEnter it on the website to activate your account."
                }
                """.formatted(mailFrom, toEmail, code);

            String response = webClient.post()
                .uri("/v3/smtp/email")
                .header("api-key", brevoApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            System.out.println("=== EMAIL SENT: " + response);

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
            System.out.println("=== ORDER INFO EMAIL SENT TO: " + email);

        } catch (Exception e) {
            System.out.println("=== EMAIL ERROR: " + e.getMessage());
        }
    }
}