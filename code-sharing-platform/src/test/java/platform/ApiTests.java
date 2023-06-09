package platform;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.json.JSONObject;
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

import java.util.*;

import static org.junit.Assert.*;
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
    public void apiGetCodeTest() {
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
    public void apiLatestShouldHideRestrictedAndDisplayOrderedTest() {
        ResponseEntity<String> postResponse = sendNewCodePost("JAVA IS BEST", 10, 5);

        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/api/code/latest", String.class);
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        JSONArray actualSnippets = documentContext.read("$..code");

        List<String> expectedSnippets = List.of(
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
    public void apiRestrictedTimeChangeTest() {
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

    @Test
    public void apiRestrictedViewsChangeTest() {
        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/api/code/5dd5fcba-3738-4732-aaa5-631bada1f215", String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        long views = Long.valueOf(documentContext.read("$.views").toString());
        assertEquals(0L, views);
    }

    @Test
    public void apiRestrictedViewsChangeLoopTest() {
        ResponseEntity<String> postResponse = sendNewCodePost("sendNewCodePost();", 0, 100);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        String uuid = JsonPath.parse(postResponse.getBody()).read("$.id");
        postResponse = sendNewCodePost("sendNewCodePost(); // comment", 1000, 5);
        String uuid2 = JsonPath.parse(postResponse.getBody()).read("$.id");

        for (int i = 0; i < 100; i++) {
            ResponseEntity<String> getResponse = restTemplate
                    .getForEntity("/api/code/%s".formatted(uuid), String.class);
            assertEquals(HttpStatus.OK, getResponse.getStatusCode());
            DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
            long views = Long.valueOf(documentContext.read("$.views").toString());

            assertEquals(100 - (i + 1), views);

            if (i < 5) {
                getResponse = restTemplate.getForEntity("/api/code/%s".formatted(uuid2), String.class);
                assertEquals(HttpStatus.OK, getResponse.getStatusCode());

                documentContext = JsonPath.parse(getResponse.getBody());
                views = Long.valueOf(documentContext.read("$.views").toString());

                assertEquals(5 - (i + 1), views);
            }
        }
        for (int i = 0; i < 10; i++) {
            ResponseEntity<String> getResponse = restTemplate
                    .getForEntity("/api/code/%s".formatted(uuid), String.class);
            assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());

            getResponse = restTemplate
                    .getForEntity("/api/code/%s".formatted(uuid2), String.class);
            assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
        }
    }

    // below test is useless as '/latest' endpoint
    // should not display any restricted / to-be-restricted snippets
//    @Test
//    public void apiGetLatestShouldAffectRestrictionsTest() {
//        List<Long> expectedViews = new ArrayList<>(List.of(0l, 0l, 0l, 0l));
//        List<Long> notExpectedTime = new ArrayList<>(List.of(
//                        this.codeRepository.findByNumId(6L).get(),
//                        this.codeRepository.findByNumId(5L).get(),
//                        this.codeRepository.findByNumId(9L).get(),
//                        this.codeRepository.findByNumId(2L).get())
//                .stream()
//                .map(obj -> (obj.getTime()))
//                .toList());
//        List<Code> postedCodes = new ArrayList<>();
//
//        String snippet = "xyz";
//        for (int i = 0; i < 3; i++) {
//
//            Random random = new Random();
//            snippet += 'z';
//            long time = random.nextInt(1000) + 60;
//            long views = random.nextInt(5) + 1;
//
//            ResponseEntity<String> postResponse = sendNewCodePost(snippet, time, views);
//            UUID uuid = UUID.fromString(JsonPath.parse(postResponse.getBody()).read("$.id"));
//            Code code = codeRepository.findById(uuid).get();
//
//            postedCodes.add(code);
//            expectedViews.add(0, views - 1);
//            notExpectedTime.add(0, time);
//        }
//
//        try {
//            Thread.sleep(1000);
//        } catch(InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ResponseEntity<String> getResponse = restTemplate
//                .getForEntity("/api/code/latest", String.class);
//        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
//
//        JSONArray actualViews = documentContext.read("$..views");
//        JSONArray actualTime = documentContext.read("$..time");
//
//        assertEquals(Arrays.toString(expectedViews.toArray()),
//                Arrays.toString(actualViews.subList(0, actualViews.size()).toArray()));
//
//        assertNotEquals(Arrays.toString(notExpectedTime.toArray()),
//                Arrays.toString(actualTime.subList(0, actualTime.size()).toArray()));
//    }

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
