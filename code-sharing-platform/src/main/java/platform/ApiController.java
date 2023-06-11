package platform;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ApiController {

    private boolean initialized;
    private List<Code> codeHistory;

    public ApiController() {
        this.initialized = false;
        this.codeHistory = new ArrayList<>();
    }

    @GetMapping(value = {"/api/code", "/api/code/"})
    public ResponseEntity getApiCode() {
        if(!initialized) {
            Code code = new Code("int a = 0;");
            codeHistory.add(code);
            this.initialized = true;
        }
        return ResponseEntity.ok(codeHistory.get(codeHistory.size() - 1));
    }
}
