package platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodeSharingPlatformApplication {

	public static Code latestCode = new Code("int a = 0;");

	public static void main(String[] args) {
		SpringApplication.run(CodeSharingPlatformApplication.class, args);
	}

}
