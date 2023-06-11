package platform;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiController {

    @GetMapping(value = {"/api/code", "/api/code/"})
    public ResponseEntity getApiCode() {
        return ResponseEntity.ok(CodeSharingPlatformApplication.latestCode);
    }
}
