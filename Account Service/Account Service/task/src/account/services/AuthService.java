package account.services;

import account.DTO.UserDTO;
import account.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    public User registerUser(UserDTO userDTO) {
        if(!isEmailValid(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = new User(userDTO);
        return user;
    }

    private boolean isEmailValid(String email) {
        return email.endsWith("@acme.com");
    }
}
