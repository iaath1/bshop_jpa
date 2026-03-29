package com.bshop_jpa.demo.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/novapost")
public class NovaPostApi {

    @Value("${novapost.api.key}")
    private String novaPostApiKey;

    @Value("${novapost.api.url}")
    private String novaPostApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/warehouses")
    public ResponseEntity<?> getWarehouses(@RequestBody Map<String, Object> body) {

    String city = (String) body.get("city");
    String search = (String) body.getOrDefault("search", "");

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("apiKey", novaPostApiKey);
    requestBody.put("modelName", "Address");
    requestBody.put("calledMethod", "getWarehouses");

    Map<String, Object> props = new HashMap<>();

    props.put("CityName", city);

    if (!search.isEmpty()) {
        props.put("FindByString", search);   // ⚡ ключевая оптимизация
    }

    props.put("Limit", 200); // ⚡ ограничение

    requestBody.put("methodProperties", props);

    try {

        ResponseEntity<Map> response =
                restTemplate.postForEntity(novaPostApiUrl, requestBody, Map.class);

        return ResponseEntity.ok(response.getBody());

    } catch (Exception e) {

        return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "error", "NovaPost API error"));
    }
}
}
