package platform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import platform.CodeSharingPlatformApplication;
import platform.models.Code;
import platform.repositories.CodeRepository;

@Controller
public class WebController {

    private CodeRepository codeRepository;

    public WebController(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @GetMapping("/code/new")
    public String getCreateCode() {
        return "new_code";
    }

    @GetMapping("/code/{N}")
    public String getNCode(@PathVariable("N") Long id, Model model) {
        Code code = codeRepository.get(id);
        model.addAttribute("code", code.getCode());
        model.addAttribute("date", code.getDate());
        return "index";
    }
}