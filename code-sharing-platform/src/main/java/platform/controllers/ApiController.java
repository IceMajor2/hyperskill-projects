package platform.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import platform.CodeSharingPlatformApplication;

@Controller
public class ApiController {

    @GetMapping(value = {"/api/code", "/api/code/"})
    public ResponseEntity getApiCode() {
        return ResponseEntity.ok(CodeSharingPlatformApplication.latestCode);
    }
}
