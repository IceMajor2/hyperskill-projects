package com.example.demo;

import org.springframework.stereotype.Component;
import java.util.Random;
import static com.example.demo.PasswordConfig.PasswordCharSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class PasswordGenerator {
    
    private static PasswordCharSet chars;
    private static Random random;
    
    public PasswordGenerator(@Qualifier("noDigitMarks") PasswordCharSet chars) {
        this.chars = chars;
        this.random = new Random();
    }
    
    public String generate(int length) {
        StringBuilder password = new StringBuilder("");
        String chars = this.chars.getCharacters();
        for(int i = 0; i < length; i++) {
            int charIndex = random.nextInt(chars.length());
            password.append(chars.charAt(charIndex));
        }
        return password.toString();
    }
    
}
