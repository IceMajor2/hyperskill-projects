package antifraud.web;

import antifraud.model.User;
import antifraud.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);
        return ResponseEntity.created(new URI("/api/auth/" + createdUser.getUsername()))
                .body(createdUser);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity deleteUser(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.ok(Map.of("username", username, "status", "Deleted successfully!"));
        }
        return ResponseEntity.notFound().build();
    }
}
