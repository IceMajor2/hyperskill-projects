package platform.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import platform.dtos.CodeDTO;
import platform.CodeSharingPlatformApplication;
import platform.models.Code;

@Controller
public class ApiController {

    @GetMapping(value = {"/api/code", "/api/code/"})
    public ResponseEntity getCode() {
        return ResponseEntity.ok(CodeSharingPlatformApplication.latestCode);
    }

    @PostMapping(value = {"/api/code/new", "/api/code/new/"})
    public ResponseEntity<String> postNewCode(@RequestBody @Validated CodeDTO newCode) {
        CodeSharingPlatformApplication.latestCode = new Code(newCode);
        return ResponseEntity.ok("{}");
    }
}
