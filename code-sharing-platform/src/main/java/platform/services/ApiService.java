package platform.services;

import org.springframework.stereotype.Service;
import platform.dtos.CodeRequestDTO;
import platform.dtos.CodeResponseDTO;
import platform.models.Code;
import platform.repositories.CodeRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ApiService {

    private CodeRepository codeRepository;

    public ApiService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public UUID postNewCode(CodeRequestDTO newCode) {
        if(codeRepository.existsByCode(newCode.getCode())) {
            return null;
        }
        Code code = new Code(newCode);
        Code savedCode = codeRepository.save(code);
        return savedCode.getId();
    }

    public Code getCode(String id) {
        return this.codeRepository.findById(UUID.fromString(id)).get();
    }

    public List<Code> getLatestCodes() {
        return this.codeRepository.findFirst10ByRestrictedFalseOrderByDateDesc();
    }
}
