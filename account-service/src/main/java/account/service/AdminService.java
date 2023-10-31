package account.service;

import account.dto.RoleDTO;
import account.dto.UserActionDTO;
import account.enumerated.OperationType;
import account.enumerated.Roles;
import account.exception.auth.LockAdminException;
import account.exception.auth.UserNotFoundException;
import account.exception.roles.*;
import account.model.User;
import account.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private UserRepository userRepository;
    private Map<String, Integer> attempts;

    public AdminService(UserRepository userRepository, Map<String, Integer> attempts) {
        this.userRepository = userRepository;
        this.attempts = attempts;
    }

    public List<User> getUsersList() {
        List<User> users = userRepository.findAllByOrderByIdAsc();
        return users;
    }

    public User deleteUser(String email) {
        User user = getUserOrElseThrow(email);
        if (user.isAdmin()) {
            throw new AdminDeletionException();
        }
        userRepository.delete(user);
        return user;
    }

    public User changeRole(RoleDTO roleDTO) {
        User user = getUserOrElseThrow(roleDTO.getEmail());
        OperationType op = roleDTO.getOperation();
        Roles role = parseRole(roleDTO.getRole());

        checkConditions(op, user, role);

        if (op == OperationType.GRANT) {
            user.addRole(role);
            userRepository.save(user);
            return user;
        }

        user.removeRole(role);
        userRepository.save(user);
        return user;
    }

    public User lockUnlockUser(UserActionDTO userActionDTO) {
        User user = getUserOrElseThrow(userActionDTO.getEmail());
        adminLockCondition(user);

        user.setAccountNonLocked(userActionDTO.getOperation().accountShouldBeNonLocked());
        if(userActionDTO.getOperation().accountShouldBeNonLocked() == true) {
            attempts.put(user.getEmail(), 0);
        }
        userRepository.save(user);
        return user;
    }

    public static Roles parseRole(String role) {
        Roles roleEnum = null;
        try {
            roleEnum = Roles.valueOf("ROLE_" + role);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return roleEnum;
    }

    private User getUserOrElseThrow(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private void checkConditions(OperationType op, User user, Roles role) {
        if (op == OperationType.REMOVE) {
            unpossessedRoleRemovalCondition(user, role);
            adminDeleteCondition(user);
            tooLittleRolesCondition(user);
            return;
        }
        roleGroupComboCondition(user, role);
        roleAllocatedCondition(user, role);
    }

    private void unpossessedRoleRemovalCondition(User user, Roles role) throws RoleNotFoundException {
        if (!user.hasRole(role)) {
            throw new RoleNotFoundException();
        }
    }

    private void adminDeleteCondition(User user) throws AdminDeletionException {
        if (user.isAdmin()) {
            throw new RoleNotFoundException();
        }
    }

    private void adminLockCondition(User user) {
        if(user.isAdmin()) {
            throw new LockAdminException();
        }
    }

    private void tooLittleRolesCondition(User user) throws TooLittleRolesException {
        if (user.rolesCount() == 1) {
            throw new TooLittleRolesException();
        }
    }

    private void roleGroupComboCondition(User user, Roles role) throws RoleGroupCombinationException {
        if ((role.isAdmin() && !user.isAdmin())
                || (!role.isAdmin() && user.isAdmin())) {
            throw new RoleGroupCombinationException();
        }
    }

    private void roleAllocatedCondition(User user, Roles role) throws RoleAlreadyAllocatedException {
        if (user.hasRole(role)) {
            throw new RoleAlreadyAllocatedException();
        }
    }
}
