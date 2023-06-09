package platform;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodeSharingPlatformApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void shouldReturnCorrectHeaders() {
        ResponseEntity response = restTemplate.getForEntity("/", String.class);

        Assert.assertEquals(MediaType.TEXT_HTML,
                response.getHeaders().getContentType());
    }

    @Test
    public void titleShouldBeCodeTest() {
        String response = restTemplate.getForObject("/", String.class);
        Document doc = Jsoup.parse(response);

        Assert.assertEquals("Code", doc.title());
    }

    @Test
    public void preTagShouldNotBeEmpty() {
        String response = restTemplate.getForObject("/", String.class);
        Document doc = Jsoup.parse(response);

        Elements elements = doc.select("pre");
        Assert.assertTrue(elements.hasText());
    }
}
