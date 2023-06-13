package platform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import platform.services.WebService;

@Controller
public class WebController {

    private WebService webService;

    public WebController(WebService webService) {
        this.webService = webService;
    }

    @GetMapping("/code/new")
    public String getCreateCode() {
        return "new_code";
    }

    @GetMapping("/code/{id}")
    public String getCode(@PathVariable String id, Model model) {
        this.webService.getCode(id, model);
        return "index";
    }

    @GetMapping("/code/latest")
    public String getLatestCodes(Model model) {
        this.webService.getLatestCodes(model);
        return "latest_codes";
    }
}
