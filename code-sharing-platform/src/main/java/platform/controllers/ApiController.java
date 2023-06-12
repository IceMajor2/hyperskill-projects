package platform.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import platform.dtos.CodeDTO;
import platform.CodeSharingPlatformApplication;
import platform.models.Code;
import platform.repositories.CodeRepository;

@Controller
public class ApiController {

    private CodeRepository codeRepository;

    public ApiController(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @PostMapping(value = {"/api/code/new", "/api/code/new/"})
    public ResponseEntity<String> postNewCode(@RequestBody @Validated CodeDTO newCode) {
        Code code = new Code(newCode);
        this.codeRepository.put(code);
        return ResponseEntity.ok("{}");
    }

    @GetMapping("/api/code/{N}")
    public ResponseEntity getNCode(@PathVariable("N") Long id) {
        Code code = this.codeRepository.get(id);
        return ResponseEntity.ok(code);
    }
}