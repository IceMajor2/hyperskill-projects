package platform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import platform.CodeSharingPlatformApplication;

@Controller
public class WebController {

    @GetMapping("/code")
    public String getLatestCode(Model model) {
        model.addAttribute("date", CodeSharingPlatformApplication.latestCode.getDate());
        model.addAttribute("code", CodeSharingPlatformApplication.latestCode.getCode());
        return "index";
    }
}
