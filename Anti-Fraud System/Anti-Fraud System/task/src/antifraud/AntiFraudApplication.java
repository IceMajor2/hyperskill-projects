package antifraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * TODO: create services to provide for controllers
 */

@SpringBootApplication
@ComponentScan
public class AntiFraudApplication {
    public static void main(String[] args) {
        SpringApplication.run(AntiFraudApplication.class, args);
    }
}