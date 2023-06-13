package platform.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import platform.dtos.CodeRequestDTO;
import platform.models.Code;
import platform.services.ApiService;

@Controller
public class ApiController {

    private ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping(value = {"/api/code/new", "/api/code/new/"})
    public ResponseEntity postNewCode(@RequestBody @Validated CodeRequestDTO newCode) {
        var response = this.apiService.postNewCode(newCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/code/{id}")
    public ResponseEntity getCode(@PathVariable String id) {
        Code code = this.apiService.getCode(id);
        return ResponseEntity.ok(code);
    }

    @GetMapping("/api/code/latest")
    public ResponseEntity getLatestCodes() {
        var codes = this.apiService.getLatestCodes();
        return ResponseEntity.ok(codes);
    }
}
