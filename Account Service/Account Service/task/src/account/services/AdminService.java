package account.services;

import account.exceptions.AdminDeletionException;
import account.exceptions.UserNotFoundException;
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

    public User deleteUser(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException());
        if(user.isAdmin()) {
            throw new AdminDeletionException();
        }
        userRepository.delete(user);
        return user;
    }
}
