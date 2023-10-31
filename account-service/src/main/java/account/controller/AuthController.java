package account.controller;

import account.dto.NewPasswordDTO;
import account.dto.UserDTO;
import account.model.User;
import account.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = {"/signup", "/signup/"})
    public User registerUser(@RequestBody @Valid UserDTO userDTO) {
        User user = authService.registerUser(userDTO);
        return authService.registerUser(userDTO);
    }

    @PostMapping(value = {"/changepass", "/changepass/"})
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT') or hasAuthority('ROLE_ADMINISTRATOR')")
    public Map<String, String> changePassword(@AuthenticationPrincipal UserDetails details,
                                         @RequestBody @Valid NewPasswordDTO passwordDTO) {
        authService.changePassword(details, passwordDTO);
        return Map.of("email", details.getUsername(), "status", "The password has been updated successfully");
    }
}
