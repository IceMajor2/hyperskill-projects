package account.services;

import account.DTO.NewPasswordDTO;
import account.DTO.UserDTO;
import account.enums.Roles;
import account.exceptions.auth.BreachedPasswordException;
import account.exceptions.auth.PasswordNotChangedException;
import account.exceptions.auth.UserExistsException;
import account.models.User;
import account.repositories.BreachedPasswordsRepository;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BreachedPasswordsRepository breachedPasswordsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserDTO userDTO) {
        passwordBreachedCondition(userDTO.getPassword());
        userExistsCondition(userDTO.getEmail());

        User user = new User(userDTO);
        assignRole(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return user;
    }

    public User changePassword(UserDetails userDetails, NewPasswordDTO newPasswordDTO) {
        passwordBreachedCondition(newPasswordDTO.getPassword());
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).get();
        differentPasswordCondition(user.getPassword(), newPasswordDTO.getPassword());

        user.setPassword(passwordEncoder.encode(newPasswordDTO.getPassword()));
        userRepository.save(user);
        return user;
    }

    private void assignRole(User user) {
        if (userRepository.count() == 0) {
            user.addRole(Roles.ROLE_ADMINISTRATOR);
            return;
        }
        user.addRole(Roles.ROLE_USER);
    }

    private void passwordBreachedCondition(String password) {
        var optPass = breachedPasswordsRepository.findByPassword(password);
        if (optPass.isPresent()) {
            throw new BreachedPasswordException();
        }
    }

    private void userExistsCondition(String email) {
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new UserExistsException();
        }
    }

    private void differentPasswordCondition(String usrPassHash, String newPass) {
        if (passwordEncoder.matches(newPass, usrPassHash)) {
            throw new PasswordNotChangedException();
        }
    }
}
