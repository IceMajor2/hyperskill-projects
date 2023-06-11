package platform;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/code")
    public String getLatestCode(Model model) {
        model.addAttribute("code", CodeSharingPlatformApplication.latestCode);
        return "index.html";
    }
}
