package platform;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class CodeSharingPlatformController {

    @GetMapping(value = {"/code", "/codes"})
    public String getCode() {
        // no need to explicitly define headers. Spring, I guess, does that for me
        return "index";
    }

    @GetMapping(value = {"/api/code", "/api/code/"})
    public ResponseEntity getApiCode() {
        String code = "public static void main(String[] args) {\n" +
                "    SpringApplication.run(CodeSharingPlatform.class, args);\n" +
                "}";
        return ResponseEntity.ok(Map.of("code", code));
    }
}
