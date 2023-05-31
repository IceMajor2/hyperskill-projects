package account.services;

import account.DTO.NewPasswordDTO;
import account.DTO.UserDTO;
import account.exceptions.BreachedPasswordException;
import account.exceptions.PasswordNotChangedException;
import account.exceptions.UserExistsException;
import account.models.User;
import account.repositories.BreachedPasswordsRepository;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BreachedPasswordsRepository breachedPasswordsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserDTO userDTO) {
        validateCredentials(userDTO);

        User user = new User(userDTO);
        assignRole(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public void changePassword(UserDetails userDetails, NewPasswordDTO newPasswordDTO) {
        if (isPasswordBreached(newPasswordDTO.getPassword())) {
            throw new BreachedPasswordException();
        }
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).get();
        if (!isPasswordDifferent(user.getPassword(), newPasswordDTO.getPassword())) {
            throw new PasswordNotChangedException();
        }

        user.setPassword(passwordEncoder.encode(newPasswordDTO.getPassword()));
        userRepository.save(user);
    }

    private void validateCredentials(UserDTO userDTO) {
        if (isPasswordBreached(userDTO.getPassword())) {
            throw new BreachedPasswordException();
        }

        if (userRepository.findByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new UserExistsException();
        }
    }

    private void assignRole(User user) {
        if (userRepository.count() == 0) {
            user.addRole("ROLE_ADMINISTRATOR");
            return;
        }
        user.addRole("ROLE_USER");
    }

    private boolean isPasswordBreached(String password) {
        var optPass = breachedPasswordsRepository.findByPassword(password);
        return optPass.isPresent();
    }

    private boolean isPasswordDifferent(String userHashed, String password) {
        return !passwordEncoder.matches(password, userHashed);
    }
}
