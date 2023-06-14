package platform;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import platform.repositories.CodeRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
}