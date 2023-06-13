package platform.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import platform.dtos.CodeRequestDTO;
import platform.dtos.CodeResponseDTO;
import platform.models.Code;
import platform.repositories.CodeRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class ApiController {

    private CodeRepository codeRepository;

    public ApiController(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @PostMapping(value = {"/api/code/new", "/api/code/new/"})
    public ResponseEntity postNewCode(@RequestBody @Validated CodeRequestDTO newCode) {
        if(codeRepository.existsByCode(newCode.getCode())) {
            return null;
        }
        Code code = new Code(newCode);
        Code savedCode = codeRepository.save(code);
        CodeResponseDTO codeResponseDTO = new CodeResponseDTO(savedCode);
        return ResponseEntity.ok(codeResponseDTO);
    }

    @GetMapping("/api/code/{id}")
    public ResponseEntity getNCode(@PathVariable String id) {
        Code code = this.codeRepository.findById(UUID.fromString(id)).get();
        return ResponseEntity.ok(code);
    }

    @GetMapping("/api/code/latest")
    public ResponseEntity getLatestCodes() {
        return ResponseEntity.ok(codeRepository.findFirst10ByRestrictedFalseOrderByDateDesc());
    }
}
