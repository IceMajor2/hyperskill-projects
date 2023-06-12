package platform.repositories;

import org.springframework.context.annotation.Configuration;
import platform.models.Code;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CodeRepository {

    private Map<Long, Code> codeRepository;

    public CodeRepository() {
        this.codeRepository = new HashMap<>();
    }

    public void put(Long id, Code code) {
        this.codeRepository.put(id, code);
    }

    public void put(Code code) {
        Long id = this.codeRepository.size() + 1L;
        this.codeRepository.put(id, code);
    }

    public int size() {
        return this.codeRepository.size();
    }

    public Code get(Long id) {
        return this.codeRepository.get(id);
    }
}
