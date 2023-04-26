package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {
    
    private final PasswordGenerator generator;
    
    @Autowired
    public Runner(PasswordGenerator generator) {
        this.generator = generator;
    }
    
    @Override
    public void run(String... args) {
        System.out.println(String.format("We run! (Password is: '%s')",
                generator.generate(10)));
    }
}
