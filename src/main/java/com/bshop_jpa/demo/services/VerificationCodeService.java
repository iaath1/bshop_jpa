package com.bshop_jpa.demo.services;

import java.time.Duration;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

    private static final Logger log = LoggerFactory.getLogger(VerificationCodeService.class);
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    public String generateCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(900000);

        return String.valueOf(code);
    }

    public void saveCode(String email, String code) {
        redisTemplate.opsForValue().set(
            "verify:" + email,
            code,
            Duration.ofMinutes(10)
        );

        log.debug("Verification code saved for: {}", email);
    }

    public boolean verifyCode(String email, String code) {
        String savedCode = redisTemplate.opsForValue().get("verify:" + email);

        if(savedCode == null) {
            log.debug("No code found in Redis for: {}", email);
            return false;
        }

        return savedCode.equals(code);
    }

    public void deleteCode(String email) {
        redisTemplate.delete("verify:" + email);
    }
}
