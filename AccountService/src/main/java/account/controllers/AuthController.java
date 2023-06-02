package account.controllers;

import account.DTO.NewPasswordDTO;
import account.DTO.UserDTO;
import account.models.User;
import account.services.AuthService;
import account.services.SecurityLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private SecurityLogService securityLogService;

    @PostMapping(value = {"/signup", "/signup/"})
    public ResponseEntity registerUser(@RequestBody @Valid UserDTO userDTO) {
        User user = authService.registerUser(userDTO);
        securityLogService.saveCreateUserLog(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = {"/changepass", "/changepass/"})
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT') or hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity changePassword(@AuthenticationPrincipal UserDetails details,
                                         @RequestBody @Valid NewPasswordDTO passwordDTO) {
        User user = authService.changePassword(details, passwordDTO);
        securityLogService.saveChangePasswordLog(user);
        return ResponseEntity.ok(Map.of("email", details.getUsername(),
                "status", "The password has been updated successfully"));
    }
}
