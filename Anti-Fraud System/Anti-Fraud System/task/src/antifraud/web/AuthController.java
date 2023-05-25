package antifraud.web;

import antifraud.DTO.OperationDTO;
import antifraud.DTO.RoleDTO;
import antifraud.DTO.UserDTO;
import antifraud.model.User;
import antifraud.repository.UserRepository;
import antifraud.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@ComponentScan
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_SUPPORT') or hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity listUsers() {
        var users = authService.listUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity createUser(@Valid @RequestBody User user) throws URISyntaxException {
        try {
            UserDTO createdUser = authService.createUser(user);
            return ResponseEntity.created(new URI("/api/auth/" + createdUser.getUsername()))
                    .body(createdUser);
        } catch(ResponseStatusException exception) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity deleteUser(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.ok(Map.of("username", username, "status", "Deleted successfully!"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/access")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity access(@RequestBody OperationDTO operationDTO) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(operationDTO.getUsername());
        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User foundUser = user.get();
        if(foundUser.getRole().toLowerCase().equals("administrator")) {
            return ResponseEntity.badRequest().build();
        }
        boolean lock = operationDTO.equals("LOCK") ? true : false;
        foundUser.setAccountNonLocked(!lock);
        userRepository.save(foundUser);
        return ResponseEntity.ok(Map.of("status", "User %s %s!"
                .formatted(foundUser.getUsername(), lock == true ? "locked" : "unlocked")));
    }

    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity role(@RequestBody RoleDTO roleDTO) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(roleDTO.getUsername());
        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User foundUser = user.get();
        if(!(roleDTO.getRole().equals("SUPPORT") || roleDTO.getRole().equals("MERCHANT"))) {
            return ResponseEntity.badRequest().build();
        }
        if(roleDTO.getRole().equals(foundUser.getRole())) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        foundUser.setRole(roleDTO.getRole());
        userRepository.save(foundUser);
        return ResponseEntity.ok(Map.of("id", foundUser.getId(),
                "name", foundUser.getName(),
                "username", foundUser.getUsername(),
                "role", foundUser.getRole()));
    }
}
