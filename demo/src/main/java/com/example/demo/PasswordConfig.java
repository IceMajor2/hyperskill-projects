package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class PasswordConfig {

    private static final String LATIN_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-=|}]{[:;'?/>.<,'";

    @Primary
    @Bean
    public PasswordCharSet allMarks() {
        return new PasswordCharSet(LATIN_CHARACTERS + DIGITS + SPECIAL_CHARACTERS);
    }
    
    @Bean
    public PasswordCharSet noDigitMarks() {
        return new PasswordCharSet(LATIN_CHARACTERS + SPECIAL_CHARACTERS);
    }
    
    static class PasswordCharSet {

        private String charSet;
        
        public PasswordCharSet(String set) {
            this.charSet = set;
        }
        
        public String getCharacters() {
            return this.charSet;
        }
    }

}
