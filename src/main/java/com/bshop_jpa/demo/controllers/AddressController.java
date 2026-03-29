package com.bshop_jpa.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
public class AddressController {
    @Value("${google.api.key}")
    private String googleApiKey;

    @GetMapping("/api/google-maps/js")
    public ResponseEntity<String> getGoogleMapsJs() {
        String scriptUrl =  "https://maps.googleapis.com/maps/api/js?key=" + googleApiKey + "&callback=initMap&language=pl&region=PL";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(scriptUrl, String.class);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }


}
