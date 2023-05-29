package account.controllers;

import account.DTO.UserDTO;
import account.models.User;
import account.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = {"/signup", "/signup/"})
    public ResponseEntity registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            User user = authService.registerUser(userDTO);
            return ResponseEntity.ok(Map.of("id", user.getId(),
                    "name", user.getName(),
                    "lastname", user.getLastName(),
                    "email", user.getEmail()));
        } catch (ResponseStatusException exception) {
            throw exception;
        }
    }

    @PostMapping("/changepass")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT') or hasAuthority('ROLE_ADMINISTRATOR')")
    public void changePassword() {

    }
}
