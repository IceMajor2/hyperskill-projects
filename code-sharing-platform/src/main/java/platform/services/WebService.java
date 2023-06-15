package platform.services;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import platform.models.Code;
import platform.repositories.CodeRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WebService {

    private CodeRepository codeRepository;

    public WebService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public boolean getCode(String uuid, Model model) {
        Optional<Code> optCode = codeRepository.findById(UUID.fromString(uuid));
        if(optCode.isEmpty()) {
            return false;
        }
        Code code = optCode.get();
        code.updateRestrictions();
        codeRepository.save(code);
        if(code.isRestricted()) {
            codeRepository.delete(code);
            return false;
        }
        model.addAttribute("code", code);
//        model.addAttribute("code", code.getCode());
//        model.addAttribute("date", code.getDateFormatted());
        return true;
    }

    public void getLatestCodes(Model model) {
        List<Code> database = this.codeRepository.findAll();
        for(Code code : database) {
            code.updateRestrictions();
            this.codeRepository.save(code);
        }
        List<Code> codes = this.codeRepository.findFirst10ByToBeTimeRestrictedFalseAndToBeViewRestrictedFalseOrderByDateDesc();
        model.addAttribute("codes", codes);
    }
}
