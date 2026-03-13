package com.bshop_jpa.demo.services;

import java.time.Duration;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {
    
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

        System.out.println("Saved code for " + email + ": " + code);
    }

    public boolean verifyCode(String email, String code) {
        String savedCode = redisTemplate.opsForValue().get("verify:" + email);

        if(savedCode == null) {
            System.out.println("VerifyCode: savedCode == null");
            return false;
        }
        System.out.println("Verify code for " + email + ": " + code);

        System.out.println("SavedCode.equals(code): " + savedCode.equals(code));

        return savedCode.equals(code);
    }

    public void deleteCode(String email) {
        redisTemplate.delete("verify:" + email);
    }
}
