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
import java.util.Optional;

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
        setDefaultUserRole(user);
        userRepository.save(user);
        User createdUser = userRepository.findByUsernameIgnoreCase(user.getUsername()).get();
        return new UserDTO(createdUser);
    }

    private void setUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void setDefaultUserRole(User user) {
        if(userRepository.count() == 0) {
            user.setRole("ADMINISTRATOR");
            user.setAccountNonLocked(true);
            return;
        }
        user.setRole("MERCHANT");
    }

    public UserDTO deleteUser(String username) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        if(user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userRepository.delete(user.get());
        return new UserDTO(user.get());
    }

    public UserDTO changeLocking(String username, String operation) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        if(user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User foundUser = user.get();
        if(foundUser.getRole().toLowerCase().equals("administrator")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        boolean lock = operation.equals("LOCK") ? true : false;
        foundUser.setAccountNonLocked(!lock);
        userRepository.save(foundUser);
        return new UserDTO(foundUser);
    }

    public UserDTO changeRole(String username, String newRole) {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
        if(user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User foundUser = user.get();
        if(!(newRole.equals("SUPPORT") || newRole.equals("MERCHANT"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(newRole.equals(foundUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        foundUser.setRole(newRole);
        userRepository.save(foundUser);
        return new UserDTO(foundUser);
    }
}
