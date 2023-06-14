package platform;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import platform.repositories.CodeRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/schema-test.sql", "/data-test.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
class HtmlTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CodeRepository codeRepository;

    @Test
    public void htmlCheckSubmitPageTest() {
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
    public void htmlTimeRestrictedCodeShouldReturnNotFoundTest() {
        ResponseEntity<String> getRestrictedResponse = restTemplate
                .getForEntity("/code/a417bb27-c069-417a-bbdb-ec6af8dff337", String.class);

        assertEquals(HttpStatus.NOT_FOUND, getRestrictedResponse.getStatusCode());
    }

    @Test
    public void htmlLatestShouldHideRestrictedTest() {
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
    public void htmlGetViewRestrictedCodeTest() {
        ResponseEntity<String> getRestrictedResponse = restTemplate
                .getForEntity("/code/5dd5fcba-3738-4732-aaa5-631bada1f215", String.class);
        assertEquals(HttpStatus.OK, getRestrictedResponse.getStatusCode());

        String html = getRestrictedResponse.getBody();
        Document doc = Jsoup.parse(html);

        Element viewsRestriction = doc.getElementById("views_restriction");
        assertEquals("span", viewsRestriction.tagName().toLowerCase());

        Element timeRestriction = doc.getElementById("time_restriction");
        assertNull("Time restriction element should not be present", timeRestriction);
    }
}