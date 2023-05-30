package account.controllers;

import account.DTO.NewPasswordDTO;
import account.DTO.UserDTO;
import account.models.User;
import account.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = {"/signup", "/signup/"})
    public ResponseEntity registerUser(@RequestBody @Valid UserDTO userDTO) {
        User user = authService.registerUser(userDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/changepass")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT') or hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity changePassword(@AuthenticationPrincipal UserDetails details,
                                         @RequestBody @Valid NewPasswordDTO passwordDTO) {
        authService.changePassword(details, passwordDTO);
        return ResponseEntity.ok(Map.of("email", details.getUsername(),
                "status", "The password has been updated successfully"));
    }
}
