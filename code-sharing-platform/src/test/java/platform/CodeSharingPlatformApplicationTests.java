package platform;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static platform.CustomAssertions.*;
import static platform.CustomJsonOperations.createJson;

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
    public void apiCorrectPostNewCodeResponse() {
        ResponseEntity<String> postRes = sendNewCodePost("int b = -4563;", 0, 0);

        DocumentContext documentContext = JsonPath.parse(postRes.getBody());

        String strUUID = JsonPath.parse(postRes.getBody()).read("$.id");

        assertIsUUID(strUUID);

        long time = Long.valueOf(documentContext.read("$.time").toString());
        long views = Long.valueOf(documentContext.read("$.views").toString());

        JSONObject expected = createJson("id", strUUID, "time", time, "views", views);

        assertEquals(expected.toString(), postRes.getBody());
    }

    @Test
    public void apiAccessCodeSnippetsThroughUUID() {
        ResponseEntity<String> postRes = sendNewCodePost("int b = -4563;", 0, 0);
        String strUUID = JsonPath.parse(postRes.getBody()).read("$.id");
        assertIsUUID(strUUID);

        Code posted = codeRepository.findById(UUID.fromString(strUUID)).get();
        JSONObject expected = createJson(posted);

        ResponseEntity<String> getRes = restTemplate
                .getForEntity("/api/code/%s".formatted(strUUID), String.class);
        JSONObject actual = createJson(getRes);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void checkCodeNewHtmlEndpoint() {
        String response = restTemplate
                .getForObject("/code/new", String.class);

        Document doc = Jsoup.parse(response);

        Element timeRestriction = doc.getElementById("time_restriction");
        assertEquals("input", timeRestriction.tagName().toLowerCase());

        String timeRestrictionAttribute = timeRestriction.attributes().get("type");
        assertEquals("text", timeRestrictionAttribute);

        Element viewsRestriction = doc.getElementById("views_restriction");
        assertEquals("input", viewsRestriction.tagName().toLowerCase());

        String viewsRestrictionAttribute = viewsRestriction.attributes().get("type");
        assertEquals("text", viewsRestrictionAttribute);

        Element codeSnippet = doc.getElementById("code_snippet");

        assertEquals("textarea", codeSnippet.tagName().toLowerCase());
        assertEquals("Create", doc.title());

        Element sendSnippetButton = doc.getElementById("send_snippet");
        assertEquals("button", sendSnippetButton.tagName());

        String attribute = sendSnippetButton.attributes().get("type");
        assertEquals("submit", attribute);

        String buttonText = sendSnippetButton.text();
        assertEquals("Submit", buttonText);
    }

    @Test
    public void apiGetTenLatestCodeSnippetsOrderDesc() {
        sendNewCodePost("public static final xyz = 0;", 1, 1);
        List<String> expectedSnippets = this.codeRepository.findFirst10ByRestrictedFalseOrderByDateDesc()
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
        List<String> expectedSnippets = this.codeRepository.findFirst10ByRestrictedFalseOrderByDateDesc()
                .stream()
                .map(obj -> obj.getCode())
                .toList();

        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/code/latest", String.class);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        JSONArray actualSnippets = documentContext.read("$..code");

        assertEquals(Arrays.toString(expectedSnippets.toArray()),
                Arrays.toString(actualSnippets.subList(0, actualSnippets.size()).toArray()));

        List<String> expectedDates = this.codeRepository.findFirst10ByRestrictedFalseOrderByDateDesc()
                .stream()
                .map(obj -> obj.getDateFormatted())
                .toList();

        JSONArray actualDates = documentContext.read("$..date");
        assertEquals(Arrays.toString(expectedDates.toArray()),
                Arrays.toString(actualDates.subList(0, actualDates.size()).toArray()));
    }

    @Test
    public void htmlGetTenLatestCodeSnippetsOrderDesc() {
        List<String> expectedSnippets = this.codeRepository.findFirst10ByRestrictedFalseOrderByDateDesc()
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

        List<String> expectedDates = this.codeRepository.findFirst10ByRestrictedFalseOrderByDateDesc()
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

    @Test
    public void apiLatestShouldNotShowRestrictedSnippets() {
        List<String> expectedSnippets = List.of(
                        this.codeRepository.findByNumId(6L).get(),
                        this.codeRepository.findByNumId(9L).get(),
                        this.codeRepository.findByNumId(2L).get())
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
    }

    @Test
    public void htmlLatestShouldNotShowRestrictedSnippets() {

    }

    private ResponseEntity<String> sendNewCodePost(String code, long time, long views) {
        JSONObject codeDTO = createJson("code", code, "time", time, "views", views);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(codeDTO.toString(), headers);
        ResponseEntity<String> postRes = restTemplate.
                postForEntity("/api/code/new", request, String.class);
        return postRes;
    }
}