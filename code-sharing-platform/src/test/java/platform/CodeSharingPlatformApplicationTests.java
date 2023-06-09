package platform;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodeSharingPlatformApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void titleShouldBeCodeTest() {
        String responseBody = restTemplate.getForObject("/", String.class);
        Document doc = Jsoup.parse(responseBody);

        Assert.assertEquals("Code", doc.title());
    }
}
