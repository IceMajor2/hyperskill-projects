package account.services;

import account.DTO.RoleDTO;
import account.enums.OperationType;
import account.enums.Roles;
import account.exceptions.*;
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
        if (user.isAdmin()) {
            throw new AdminDeletionException();
        }
        userRepository.delete(user);
        return user;
    }

    public User changeRole(RoleDTO roleDTO) {
        User user = userRepository.findByEmailIgnoreCase(roleDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException());

        OperationType op = roleDTO.getOperation();
        Roles role = null;
        try {
            role = Roles.valueOf("ROLE_" + roleDTO.getRole());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // START |----| exceptions
        if (op == OperationType.REMOVE && !user.hasRole(role)) {
            throw new RoleNotFoundException();
        }
        if (op == OperationType.REMOVE && user.rolesCount() == 1) {
            throw new TooLittleRolesException();
        }
        if (op == OperationType.REMOVE && user.isAdmin()) {
            throw new AdminDeletionException();
        }
        if (op == OperationType.GRANT
                && ((role.isAdmin() && !user.isAdmin())
                || (!role.isAdmin() && user.isAdmin()))) {
            throw new RoleGroupCombinationException();
        }
        if(op == OperationType.GRANT && user.hasRole(role)) {
            throw new RoleAlreadyAllocatedException();
        }
        // END |----| exceptions handled

        if (op == OperationType.GRANT) {
            user.addRole(role);
            userRepository.save(user);
            return user;
        }

        user.removeRole(role);
        userRepository.save(user);
        return user;
    }
}
