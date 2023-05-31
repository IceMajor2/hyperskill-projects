package account.services;

import account.models.User;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsersList() {
        List<User> users = userRepository.findAllByOrderByIdAsc();
        return users;
    }
}
