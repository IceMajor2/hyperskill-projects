package account.services;

import account.DTO.UserDTO;
import account.exceptions.UserExistsException;
import account.models.User;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(UserDTO userDTO) {
        if(!isEmailValid(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if(userRepository.findByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new UserExistsException();
        }

        User user = new User(userDTO);
        return user;
    }

    private boolean isEmailValid(String email) {
        return email.endsWith("@acme.com");
    }
}
