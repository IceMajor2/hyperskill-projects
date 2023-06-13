package platform.services;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import platform.models.Code;
import platform.repositories.CodeRepository;

import java.util.List;
import java.util.UUID;

@Service
public class WebService {

    private CodeRepository codeRepository;

    public WebService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public void getCode(String uuid, Model model) {
        Code code = codeRepository.findById(UUID.fromString(uuid)).get();
        model.addAttribute("code", code.getCode());
        model.addAttribute("date", code.getDateFormatted());
    }

    public void getLatestCodes(Model model) {
        List<Code> codes = this.codeRepository.findFirst10ByRestrictedFalseOrderByDateDesc();
        model.addAttribute("codes", codes);
    }
}
