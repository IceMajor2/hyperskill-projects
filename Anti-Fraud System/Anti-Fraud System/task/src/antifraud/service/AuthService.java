package antifraud.service;

import antifraud.DTO.UserDTO;
import antifraud.model.User;
import antifraud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public UserDTO createUser(User user) {
        if(userRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        setUserPassword(user);
        setUserRole(user);
        userRepository.save(user);
        User createdUser = userRepository.findByUsernameIgnoreCase(user.getUsername()).get();
        return new UserDTO(createdUser);
    }

    private void setUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void setUserRole(User user) {
        if(userRepository.count() == 0) {
            user.setRole("ADMINISTRATOR");
            user.setAccountNonLocked(true);
            return;
        }
        user.setRole("MERCHANT");
    }
}
