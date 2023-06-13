package platform;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import platform.models.Code;
import platform.repositories.CodeRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/schema-test.sql", "/data-test.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
class CodeSharingPlatformApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CodeRepository codeRepository;

    @Test
    public void apiCorrectPostNewCodeResponse() throws JSONException {
        ResponseEntity<String> postRes = sendNewCodePost("int b = -4563;");
        String strUUID = JsonPath.parse(postRes.getBody()).read("$.id");

        JSONObject expected = createJson("id", strUUID);

        assertEquals(expected.toString(), postRes.getBody());
    }

    @Test
    public void checkCodeNewHtmlEndpoint() {
        String response = restTemplate
                .getForObject("/code/new", String.class);

        Document doc = Jsoup.parse(response);

        Element codeSnippet = doc.getElementById("code_snippet");

        assertEquals(codeSnippet.tagName().toLowerCase(), "textarea");
        assertEquals("Create", doc.title());

        Element sendSnippetButton = doc.getElementById("send_snippet");
        assertEquals("button", sendSnippetButton.tagName());

        String attribute = sendSnippetButton.attributes().get("type");
        assertEquals("submit", attribute);

        String buttonText = sendSnippetButton.text();
        assertEquals("Submit", buttonText);
    }

    @Test
    public void getApiNCodeSnippet() throws JSONException {
        Code expected = this.codeRepository.findByNumId(1L).get();

        JSONObject expectedJson = createJson(expected);

        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/code/1", String.class);
        String actualDate = JsonPath.parse(response.getBody()).read("$.date");

        JSONObject actualJson = createJson(response);

        assertJsonEqual(expectedJson, actualJson);
        assertDateFormat(expected.getDateFormatted());
        assertDateFormat(actualDate);
    }

    @Test
    public void getHtmlNCodeSnippet() throws JSONException {
        Code expected = this.codeRepository.findByNumId(3L).get();
        JSONObject expectedJson = createJson(expected);

        String response = restTemplate
                .getForObject("/code/3", String.class);
        Document doc = Jsoup.parse(response);

        String actualCode = doc.getElementById("code_snippet").text();
        String actualDate = doc.getElementById("load_date").text();
        JSONObject actualJson = createJson("date", actualDate, "code", actualCode);

        assertJsonEqual(expectedJson, actualJson);
        assertDateFormat(expected.getDateFormatted());
        assertDateFormat(actualDate);
    }

    @Test
    public void apiGetTenLatestCodeSnippetsOrderDesc() throws JSONException {
        sendNewCodePost("public static final xyz = 0;");
        List<String> expectedSnippets = this.codeRepository.findFirst10ByOrderByDateDesc()
                .stream()
                .map(obj -> obj.getCode())
                .toList();

        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/code/latest", String.class);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        JSONArray actualSnippets = documentContext.read("$..code");

        assertEquals(Arrays.toString(expectedSnippets.toArray()),
                Arrays.toString(actualSnippets.subList(0, actualSnippets.size()).toArray()));
    }

    @Test
    public void apiGetTenLatestWhenLessThanTenElements() {
        List<String> expectedSnippets = this.codeRepository.findFirst10ByOrderByDateDesc()
                .stream()
                .map(obj -> obj.getCode())
                .toList();

        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/code/latest", String.class);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        JSONArray actualSnippets = documentContext.read("$..code");

        assertEquals(Arrays.toString(expectedSnippets.toArray()),
                Arrays.toString(actualSnippets.subList(0, actualSnippets.size()).toArray()));

        List<String> expectedDates = this.codeRepository.findFirst10ByOrderByDateDesc()
                .stream()
                .map(obj -> obj.getDateFormatted())
                .toList();

        JSONArray actualDates = documentContext.read("$..date");
        assertEquals(Arrays.toString(expectedDates.toArray()),
                Arrays.toString(actualDates.subList(0, actualDates.size()).toArray()));
    }

    @Test
    public void htmlGetTenLatestCodeSnippetsOrderDesc() {
        List<String> expectedSnippets = this.codeRepository.findFirst10ByOrderByDateDesc()
                .stream()
                .map(obj -> obj.getCode())
                .toList();

        String response = restTemplate
                .getForObject("/code/latest", String.class);
        Document doc = Jsoup.parse(response);

        Elements snippetElements = doc.getElementsByTag("pre");

        List<String> actualSnippets = new ArrayList<>();
        for (Element element : snippetElements) {
            if (element.id().equals("code_snippet")) {
                actualSnippets.add(element.text());
            }
        }
        assertEquals(expectedSnippets, actualSnippets);

        List<String> expectedDates = this.codeRepository.findFirst10ByOrderByDateDesc()
                .stream()
                .map(obj -> obj.getDateFormatted())
                .toList();
        Elements dateElements = doc.getElementsByTag("span");

        List<String> actualDates = new ArrayList<>();
        for (Element element : dateElements) {
            if (element.id().equals("load_date")) {
                actualDates.add(element.text());
            }
        }
        assertEquals(expectedDates, actualDates);
    }

    private JSONObject createJson(ResponseEntity<String> response) {
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        try {
            JSONObject actualJson = new JSONObject(documentContext.jsonString());
            return actualJson;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject createJson(Code code) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("date", code.getDateFormatted());
        json.put("code", code.getCode());
        return json;
    }

    private JSONObject createJson(String... pairs) throws JSONException {
        JSONObject json = new JSONObject();
        for (int i = 1; i < pairs.length; i += 2) {
            json.put(pairs[i - 1], pairs[i]);
        }
        return json;
    }

    private ResponseEntity<String> sendNewCodePost(String code) throws JSONException {
        JSONObject codeDTO = createJson("code", code);

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

    private void assertDateFormat(String date) {
        assertTrue("Date format is not valid. Should be yyyy/MM/dd HH:mm:ss. Was [%s]".formatted(date),
                isDateFormatValid(date));
    }

    private void assertDatesEqual(String expected, String actual) {
        assertDateFormat(actual);
        assertTrue("Dates are not equal\nExpected: [%s]\nBut was:[%s]".formatted(expected, actual),
                areDatesEqual(expected, actual));
    }

    private void assertJsonEqual(JSONObject expected, JSONObject actual) {
        assertEquals(expected.toString(), actual.toString());
    }
}