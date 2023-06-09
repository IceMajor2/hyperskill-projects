package platform;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CodeSharingPlatformController {

    @GetMapping("/code")
    public String getCode() {
        // no need to explicitly define headers. Spring, I guess, does that for me
        return "index";
    }
}
