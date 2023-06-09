package platform;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodeSharingPlatformApplicationTests {

    private static WebClient webClient;

    @BeforeAll
    public static void init() {
        webClient = new WebClient();
    }

    @AfterAll
    public static void close() {
        webClient.close();
    }

    @Test
    public void ensureRightTitleTest() throws Exception {
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage page = webClient.getPage("/code");

        Assertions.assertEquals("Code", page.getTitleText());
    }
}
