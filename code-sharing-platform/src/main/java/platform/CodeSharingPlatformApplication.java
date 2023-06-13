package platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodeSharingPlatformApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application-main");
		SpringApplication.run(CodeSharingPlatformApplication.class, args);
	}

}
