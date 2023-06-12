package platform.repositories;

import org.springframework.context.annotation.Configuration;
import platform.models.Code;

import java.util.*;

@Configuration
public class CodeRepository {

    private Map<Long, Code> codeRepository;

    public CodeRepository() {
        this.codeRepository = new LinkedHashMap<>();
    }

    public Long put(Long id, Code code) {
        this.codeRepository.put(id, code);
        return id;
    }

    public Long put(Code code) {
        Long id = this.codeRepository.size() + 1L;
        this.codeRepository.put(id, code);
        return id;
    }

    public int size() {
        return this.codeRepository.size();
    }

    public Code get(Long id) {
        return this.codeRepository.get(id);
    }

    public List<Code> getNLatest(int n) {
        int size = this.codeRepository.size();
        if(n > size) {
            return this.codeRepository.values().stream().toList();
        }
        return this.codeRepository.values().stream().toList().subList(size - n, size);
    }
}
