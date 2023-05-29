package account;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/signup")
    public void registerUser() {

    }

    @PostMapping("/changepass")
    public void changePassword() {

    }
}
