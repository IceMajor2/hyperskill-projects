package platform;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CodeSharingPlatformController {

    @GetMapping("/code")
    public ResponseEntity<String> getCode() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("index");
    }
}
