package antifraud.web;

import antifraud.model.User;
import antifraud.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@ComponentScan
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public UserController() {}

    @PostMapping("/api/auth/user")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws URISyntaxException {
        if(userRepository.findByUsernameIgnoreCase(user.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username '%s' already exists"
                    .formatted(user.getUsername()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);
        return ResponseEntity.created(new URI("/api/auth/" + createdUser.getUsername()))
                .body(createdUser);
    }
}
