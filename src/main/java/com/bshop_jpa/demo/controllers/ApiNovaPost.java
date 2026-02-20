package com.bshop_jpa.demo.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.StringHttpMessageConverter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/novapost")
@CrossOrigin(origins = "*")
public class ApiNovaPost {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${novapost.api.key}")
    private final String API_KEY = "";

    public ApiNovaPost() {
        this.restTemplate = new RestTemplate();
        // Додаємо підтримку UTF-8
        this.restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping("/warehouses")
    public ResponseEntity<?> getWarehouses(@RequestParam String city) {
        try {
            String url = "https://api.novaposhta.ua/v2.0/json/";
            
            // Формуємо тіло запиту згідно документації Nova Post API
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("apiKey", API_KEY);
            requestBody.put("modelName", "Address");
            requestBody.put("calledMethod", "getWarehouses");
            
            Map<String, String> methodProperties = new HashMap<>();
            methodProperties.put("CityName", city);
            methodProperties.put("Language", "UA"); // Можна змінити на "RU" або "EN"
            
            requestBody.put("methodProperties", methodProperties);

            // Налаштовуємо заголовки
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Виконуємо POST запит
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
            );

            // Парсимо JSON відповідь
            JsonNode root = objectMapper.readTree(response.getBody());
            
            // Перевіряємо на помилки
            if (root.has("errors") && root.get("errors").size() > 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "errors", root.get("errors")
                ));
            }

            // Формуємо відповідь
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", objectMapper.treeToValue(root.get("data"), List.class));
            
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "errors", List.of("Помилка при отриманні даних: " + e.getMessage())
                    ));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        try {
            String url = "https://api.novaposhta.ua/v2.0/json/";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("apiKey", API_KEY);
            requestBody.put("modelName", "Address");
            requestBody.put("calledMethod", "getCities");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            return ResponseEntity.ok("API працює: " + response.getStatusCode());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Помилка: " + e.getMessage());
        }
    }

    @GetMapping("/cities")
    public ResponseEntity<?> getCities(@RequestParam(required = false) String cityName) {
        try {
            String url = "https://api.novaposhta.ua/v2.0/json/";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("apiKey", API_KEY);
            requestBody.put("modelName", "Address");
            requestBody.put("calledMethod", "getCities");
            
            Map<String, String> methodProperties = new HashMap<>();
            if (cityName != null && !cityName.isEmpty()) {
                methodProperties.put("FindByString", cityName);
            }
            methodProperties.put("Page", "1");
            methodProperties.put("Limit", "50");
            
            requestBody.put("methodProperties", methodProperties);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", objectMapper.treeToValue(root.get("data"), List.class)
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "errors", List.of("Помилка: " + e.getMessage())
                    ));
        }
    }

    @GetMapping("/check-key")
    public ResponseEntity<?> checkApiKey() {
        try {
        String url = "https://api.novaposhta.ua/v2.0/json/";
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("apiKey", API_KEY);
        requestBody.put("modelName", "Address");
        requestBody.put("calledMethod", "getApiKeyInfo"); // Спеціальний метод для перевірки ключа
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String.class
        );
        
        return ResponseEntity.ok("Відповідь: " + response.getBody());
        
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Помилка: " + e.getMessage());
    }
    }
}