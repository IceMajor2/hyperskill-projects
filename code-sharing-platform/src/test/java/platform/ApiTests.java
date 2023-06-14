package platform;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
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
import static org.junit.Assert.assertTrue;
import static platform.CustomAssertions.assertIsUUID;
import static platform.CustomAssertions.assertJsonEqual;
import static platform.CustomJsonOperations.createJson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/schema-test.sql", "/data-test.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class ApiTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CodeRepository codeRepository;

    @Test
    public void apiNewCodeResponseTest() {
        ResponseEntity<String> postRes = sendNewCodePost("int b = -4563;", 0, 0);

        DocumentContext documentContext = JsonPath.parse(postRes.getBody());

        String strUUID = JsonPath.parse(postRes.getBody()).read("$.id");

        assertIsUUID(strUUID);

        JSONObject expected = createJson("id", strUUID);

        assertEquals(expected.toString(), postRes.getBody());
    }

    @Test
    public void apiAccessCodeTest() {
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
    public void apiLatestSnippetsInOrderTest() {
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
    public void apiLatestSnippetsInOrderEdgeCaseTest() {
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
    public void apiLatestShouldHideRestrictedTest() {
        ResponseEntity<String> postResponse = sendNewCodePost("JAVA IS BEST", 10, 5);
        UUID uuid = UUID.fromString(JsonPath.parse(postResponse.getBody()).read("$.id"));

        ResponseEntity<String> getPreviousPostResponse = restTemplate
                .getForEntity("/api/code/%s".formatted(uuid), String.class);
        Code justPosted = this.codeRepository.findById(uuid).get();

        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/api/code/latest", String.class);
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        JSONArray actualSnippets = documentContext.read("$..code");

        List<String> expectedSnippets = List.of(
                        justPosted,
                        this.codeRepository.findByNumId(6L).get(),
                        this.codeRepository.findByNumId(9L).get(),
                        this.codeRepository.findByNumId(2L).get())
                .stream()
                .map(obj -> obj.getCode())
                .toList();

        assertEquals(Arrays.toString(expectedSnippets.toArray()),
                Arrays.toString(actualSnippets.subList(0, actualSnippets.size()).toArray()));
    }

    @Test
    public void apiNoSuchUUIDTest() {
        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/api/code/1be36511-3c92-4a57-94d6-a0396c89d5f3", String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());

        getResponse = restTemplate.getForEntity("/api/code/1", String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    public void apiRestrictedTimeIsUpExpectNotFoundTest() {
        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/api/code/02104d9c-0c3c-4526-9fad-5902a4a4a263", String.class);
        Code expiredCode = codeRepository.findByNumId(8L).get();

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
        assertEquals(0L, expiredCode.getTime());
    }

    @Test
    public void apiRestrictedTimeChangesTest() {
        ResponseEntity<String> postResponse = sendNewCodePost("public void apiTest() {}", 15, 0);
        String uuid = JsonPath.parse(postResponse.getBody()).read("$.id");

        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/api/code/%s".formatted(uuid), String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        long time = Long.valueOf(documentContext.read("$.time").toString());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getResponse = restTemplate
                .getForEntity("/api/code/%s".formatted(uuid), String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        documentContext = JsonPath.parse(getResponse.getBody());
        long time2 = Long.valueOf(documentContext.read("$.time").toString());

        boolean condition = (time == time2 + 2) || (time == time2 + 1);
        assertTrue(condition);
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
