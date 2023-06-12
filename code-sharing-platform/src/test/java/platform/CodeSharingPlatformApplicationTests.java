package platform;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodeSharingPlatformApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;
    private String setupTime;

    @BeforeEach
    public void setup() throws JSONException {
        sendNewCodePost("int a = 0;");
        this.setupTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

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
    public void apiCorrectGetLatestCodeResponse() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/code", String.class);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String code = documentContext.read("$.code");
        String date = documentContext.read("$.date");

        assertEquals("int a = 0;", code);
        assertDatesEqual(this.setupTime, date);
    }

    @Test
    public void apiCorrectHeaders() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/code", String.class);
        var contentTypeStr = "%s/%s".formatted(response.getHeaders().getContentType().getType(),
                response.getHeaders().getContentType().getSubtype());

        Assert.assertEquals(MediaType.APPLICATION_JSON.toString(), contentTypeStr);
    }

    @Test
    public void apiCodeShouldEqualHTMLCode() {
        // get API code
        ResponseEntity<String> apiRes = restTemplate
                .getForEntity("/api/code", String.class);

        DocumentContext documentContext = JsonPath.parse(apiRes.getBody());
        String apiCode = documentContext.read("$.code");

        // get HTML code
        String htmlRes = restTemplate.getForObject("/code", String.class);
        Document doc = Jsoup.parse(htmlRes);

        Element codeSnippet = doc.getElementById("code_snippet");
        String htmlCode = codeSnippet.text();

        assertEquals(codeSnippet.tagName().toLowerCase(), "pre");
        assertEquals(apiCode, htmlCode);
    }

    @Test
    public void htmlDateDisplay() {
        String htmlRes = restTemplate.getForObject("/code", String.class);
        Document doc = Jsoup.parse(htmlRes);

        Element date = doc.getElementById("load_date");
        String dateStr = date.text();

        assertEquals(date.tagName().toLowerCase(), "span");
        assertTrue("Date is of wrong format", isDateFormatValid(dateStr));
    }

    @Test
    public void htmlDateShouldEqualToApiDate() {
        // get API date
        ResponseEntity<String> apiRes = restTemplate
                .getForEntity("/api/code", String.class);

        DocumentContext documentContext = JsonPath.parse(apiRes.getBody());
        String apiDate = documentContext.read("$.date");

        // get HTML date
        String htmlRes = restTemplate.getForObject("/code", String.class);
        Document doc = Jsoup.parse(htmlRes);

        Element date = doc.getElementById("load_date");
        String dateStr = date.text();

        assertDatesEqual(this.setupTime, dateStr);
    }

    @Test
    @DirtiesContext
    public void apiCorrectPostNewCodeResponse() throws JSONException {
        ResponseEntity<String> postRes = sendNewCodePost("int b = 9;");

        assertEquals("{}", postRes.getBody());
    }

    @Test
    @DirtiesContext
    public void apiNewCodePostShouldChangeHTMLCode() throws JSONException {
        ResponseEntity<String> apiRes = restTemplate
                .getForEntity("/api/code", String.class);

        DocumentContext documentContext = JsonPath.parse(apiRes.getBody());
        String apiCode = documentContext.read("$.code");
        StringBuilder newApiCode = new StringBuilder(apiCode)
                .append("SOMETHING");

        String htmlRes = restTemplate.getForObject("/code", String.class);
        Document doc = Jsoup.parse(htmlRes);

        String htmlCode = doc.getElementById("code_snippet").text();
        assertNotEquals(htmlCode, newApiCode.toString());

        sendNewCodePost(newApiCode.toString());

        ResponseEntity<String> newApiRes = restTemplate
                .getForEntity("/api/code", String.class);

        DocumentContext newDocumentContext = JsonPath.parse(newApiRes.getBody());
        String newApiCodeRes = newDocumentContext.read("$.code");
        String newApiDateRes = newDocumentContext.read("$.date");

        String newHtmlRes = restTemplate.getForObject("/code", String.class);
        Document newDoc = Jsoup.parse(newHtmlRes);

        String newHtmlCode = newDoc.getElementById("code_snippet").text();
        String newHtmlDate = newDoc.getElementById("load_date").text();

        assertEquals(newApiCodeRes, newHtmlCode);
        assertEquals(newApiDateRes, newHtmlDate);
    }

    @Test
    public void checkCodeNewHtmlEndpoint() {
        String response = restTemplate
                .getForObject("/code/new", String.class);

        Document doc = Jsoup.parse(response);

        Element codeSnippet = doc.getElementById("code_snippet");

        assertEquals(codeSnippet.tagName().toLowerCase(), "textarea");
        assertEquals("Code", doc.title());

        Element sendSnippetButton = doc.getElementById("send_snippet");
        assertEquals("button", sendSnippetButton.tagName());

        Element attribute = sendSnippetButton.getElementsByAttribute("type").first();
        assertEquals("submit", attribute.text());

        String buttonText = sendSnippetButton.text();
        assertEquals("Submit", buttonText);
    }

    private ResponseEntity<String> sendNewCodePost(String code) throws JSONException {
        JSONObject codeDTO = new JSONObject();
        codeDTO.put("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(codeDTO.toString(), headers);
        ResponseEntity<String> postRes = restTemplate.
                postForEntity("/api/code/new", request, String.class);
        return postRes;
    }

    private boolean areDatesEqual(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        long time1 = LocalDateTime.parse(date1, formatter).toEpochSecond(ZoneOffset.UTC);
        long time2 = LocalDateTime.parse(date2, formatter).toEpochSecond(ZoneOffset.UTC);
        if (time2 - 3 <= time1 && time1 <= time2 + 3) {
            return true;
        }
        if (time1 - 3 <= time2 && time2 <= time1 + 3) {
            return true;
        }
        return false;
    }

    private boolean isDateFormatValid(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        try {
            LocalDateTime.parse(date, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void assertDatesEqual(String expected, String actual) {
        assertTrue("Dates are not equal\nExpected: [%s]\nBut was:[%s]".formatted(expected, actual),
                areDatesEqual(expected, actual));
    }
}
