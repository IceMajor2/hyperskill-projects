package account.services;

import account.DTO.PaycheckDTO;
import account.exceptions.UserNotExistsException;
import account.models.User;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessService {

    @Autowired
    private UserRepository userRepository;

    public User getPayrolls(UserDetails userDetails) {
        var user = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if(user.isEmpty()) {
            throw new UserNotExistsException();
        }
        return user.get();
    }

    public void uploadPayroll(List<PaycheckDTO> paycheckDTOS) {

    }
}
