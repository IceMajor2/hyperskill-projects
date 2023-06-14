package platform.services;

import org.springframework.stereotype.Service;
import platform.dtos.CodeRequestDTO;
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
        Code code = this.fetchFromRepository(id);
        if(code == null) {
            return null;
        }
        code.updateRestrictions();
        codeRepository.save(code);
        if(code.isRestricted()) {
            return null;
        }
        return code;
    }

    public List<Code> getLatestCodes() {
        return this.codeRepository.findFirst10ByRestrictedFalseOrderByDateDesc();
    }

    private Code fetchFromRepository(String id) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch(IllegalArgumentException e) {
            return null;
        }
        if(!this.codeRepository.existsById(uuid)) {
            return null;
        }
        return this.codeRepository.findById(uuid).get();
    }
}
