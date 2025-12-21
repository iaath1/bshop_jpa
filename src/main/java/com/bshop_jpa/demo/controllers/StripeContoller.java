package com.bshop_jpa.demo.controllers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bshop_jpa.demo.DTO.CheckoutRequest;
import com.bshop_jpa.demo.models.Order;
import com.bshop_jpa.demo.services.OrderService;
import com.bshop_jpa.demo.services.StatusService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;

@Controller
@RequestMapping("/stripe")
public class StripeContoller {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StatusService statusService;
    
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${app.domain}")
    private String domain;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @PostMapping("/create-session")
    @ResponseBody
    public Map<String, Object> createSession(@RequestBody CheckoutRequest request, Locale locale) throws StripeException {
        Order order = orderService.findOrderById(request.getOrderId());

        SessionCreateParams.LineItem lineItem = 
        SessionCreateParams.LineItem.builder()
            .setQuantity(1l)
            .setPriceData(
                SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("pln")
                    .setUnitAmount(order.getTotalPriceGr())
                    .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("Order #" + order.getId())
                            .build()
                    )
                    .build()
            )
            .build();
        
        String language = locale.getLanguage();

        if(language.equals("uk")) {
            language = "en";
        }

        
        SessionCreateParams params = 
            SessionCreateParams.builder()
                .setLocale(SessionCreateParams.Locale.valueOf(language.toUpperCase()))
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(lineItem)
                .setSuccessUrl(domain + "/order/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(domain + "/order/payment/cancel?orderId=" + order.getId())
                .putMetadata("orderId", String.valueOf(order.getId()))
                .build();

        Session session = Session.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("id", session.getId());
        return response;
    }

    @PostMapping("/webhook")
    public @ResponseBody String webhook(@RequestHeader("Stripe-Siganture") String payloadSignature, @RequestBody String payload) {
        
        try {
            Event event = Webhook.constructEvent(payload, payloadSignature, webhookSecret);

            switch (event.getType()) {
                case "checkout.session.completed":
                    Session session = (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);

                    if(session != null) {
                        String orderIdStr = session.getMetadata().get("orderId");
                        System.out.println("PAYMENT SUCCES FOR ORDER: " + orderIdStr);
                        Order order = orderService.findOrderById(Long.parseLong(orderIdStr));
                        order.setStatus(statusService.findStatusByName("PAYED"));
                        orderService.saveOrder(order);
                    }
                    break;

            } 

            return "OK";
        }

        catch(Exception e) {
            return "ERROR";
        }
    }
}
