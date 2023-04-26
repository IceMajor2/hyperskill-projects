package com.example.demo;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class PasswordGenerator {
    
    private static final String chars = "qwertyuiop[]\';lkjhgfdsaazxcvbnm,./=-0987654321";
    private static final Random random = new Random();
    
    public String generate(int length) {
        StringBuilder password = new StringBuilder("");
        for(int i = 0; i < length; i++) {
            int charIndex = random.nextInt(chars.length());
            password.append(chars.charAt(charIndex));
        }
        return password.toString();
    }
    
}
