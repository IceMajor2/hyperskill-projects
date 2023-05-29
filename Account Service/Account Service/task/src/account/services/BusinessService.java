package account.services;

import account.models.User;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @Autowired
    private UserRepository userRepository;

    public void getPayrolls(User user) {

    }
}
