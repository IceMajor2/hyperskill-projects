package platform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest
class CodeSharingPlatformApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getCodeContainsTagPreAndTitleCode() {

    }

}
