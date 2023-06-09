package platform;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
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

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodeSharingPlatformApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void shouldReturnCorrectHeaders() {
        ResponseEntity response = restTemplate.getForEntity("/code", String.class);
        var contentTypeStr = "%s/%s".formatted(response.getHeaders().getContentType().getType(),
                response.getHeaders().getContentType().getSubtype());

        Assert.assertEquals(MediaType.TEXT_HTML.toString(), contentTypeStr);
    }

    @Test
    public void titleShouldBeCodeTest() {
        String response = restTemplate.getForObject("/code", String.class);
        Document doc = Jsoup.parse(response);

        assertEquals("Code", doc.title());
    }

    @Test
    public void preTagShouldNotBeEmpty() {
        String response = restTemplate.getForObject("/code", String.class);
        Document doc = Jsoup.parse(response);

        Elements elements = doc.select("pre");
        Assert.assertTrue(elements.hasText());
    }

    @Test
    public void apiCorrectJSONBody() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/code", String.class);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String code = documentContext.read("$.code");

        String expected = "public static void main(String[] args) {\n" +
                "    SpringApplication.run(CodeSharingPlatform.class, args);\n" +
                "}";

        assertEquals(code, expected);
    }

    @Test
    public void apiCorrectHeaders() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/code", String.class);
        var contentTypeStr = "%s/%s".formatted(response.getHeaders().getContentType().getType(),
                response.getHeaders().getContentType().getSubtype());

        Assert.assertEquals(MediaType.APPLICATION_JSON.toString(), contentTypeStr);
    }
}
