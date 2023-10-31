package account.service;

import account.dto.NewPasswordDTO;
import account.dto.UserDTO;
import account.enumerated.Roles;
import account.exception.auth.BreachedPasswordException;
import account.exception.auth.PasswordNotChangedException;
import account.exception.auth.UserExistsException;
import account.model.User;
import account.repository.BreachedPasswordsRepository;
import account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SecurityLogService securityLogService;

    private final UserRepository userRepository;
    private final BreachedPasswordsRepository breachedPasswordsRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserDTO userDTO) {
        passwordBreachedCondition(userDTO.getPassword());
        userExistsCondition(userDTO.getEmail());

        User user = new User(userDTO);
        assignRole(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        securityLogService.saveCreateUserLog(user);
        return user;
    }

    public User changePassword(UserDetails userDetails, NewPasswordDTO newPasswordDTO) {
        passwordBreachedCondition(newPasswordDTO.getPassword());
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).get();
        differentPasswordCondition(user.getPassword(), newPasswordDTO.getPassword());

        user.setPassword(passwordEncoder.encode(newPasswordDTO.getPassword()));
        userRepository.save(user);
        securityLogService.saveChangePasswordLog(user);
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
