package platform.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import platform.dtos.CodeRequestDTO;
import platform.models.Code;
import platform.services.ApiService;

import java.util.Map;

@Controller
public class ApiController {

    private ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping(value = {"/api/code/new", "/api/code/new/"})
    public ResponseEntity postNewCode(@RequestBody @Valid CodeRequestDTO newCode) {
        var response = this.apiService.postNewCode(newCode);
        System.out.println("[DEBUG] " + newCode);
        return ResponseEntity.ok(Map.of("id", response.toString()));
    }

    @GetMapping("/api/code/{id}")
    public ResponseEntity getCode(@PathVariable String id) {
        Code code = this.apiService.getCode(id);
        if(code == null) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("[DEBUG] " + code);
        return ResponseEntity.ok(code);
    }

    @GetMapping("/api/code/latest")
    public ResponseEntity getLatestCodes() {
        var codes = this.apiService.getLatestCodes();
        for(Code code : codes) {
            System.out.println("[DEBUG] " + code);
        }
        return ResponseEntity.ok(codes);
    }
}
