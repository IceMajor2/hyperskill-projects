package antifraud.web;

import antifraud.DTO.RoleDTO;
import antifraud.model.User;
import antifraud.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthController() {
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_SUPPORT') or hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity listUsers() {
        List<User> users = userRepository.findAllByOrderByIdAsc();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity createUser(@Valid @RequestBody User user) throws URISyntaxException {
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            //throw new ResponseStatusException(HttpStatus.CONFLICT);
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        if(userRepository.count() == 0) {
            user.setRole("ADMINISTRATOR");
        } else {
            user.setRole("MERCHANT");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);
        return ResponseEntity.created(new URI("/api/auth/" + createdUser.getUsername()))
                .body(createdUser);
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
    public void acces() {}

    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    public ResponseEntity role(@RequestBody RoleDTO roleDTO) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(roleDTO.getUsername());
        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User foundUser = user.get();
        if(!(foundUser.getRole().equals("SUPPORT") || foundUser.getRole().equals("MERCHANT"))) {
            return ResponseEntity.badRequest().build();
        }
        if(roleDTO.getRole().equals(foundUser.getRole())) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(Map.of("id", foundUser.getId(),
                "name", foundUser.getName(),
                "username", foundUser.getUsername(),
                "role", foundUser.getRole()));
    }
}
