package account.service;

import account.dto.RoleDTO;
import account.dto.UserActionDTO;
import account.enumerated.AccountAction;
import account.enumerated.OperationType;
import account.enumerated.Roles;
import account.exception.auth.LockAdminException;
import account.exception.auth.UserNotFoundException;
import account.exception.roles.AdminDeletionException;
import account.exception.roles.TooLittleRolesException;
import account.model.User;
import account.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    private AdminService SUT;

    private UserRepository userRepository;
    private UserDetails userDetails;

    private Long ANY_LONG = 1L;
    private String ANY_USERNAME = "ANY_USERNAME";
    private String ANY_LASTNAME = "ANY_LASTNAME";
    private String ANY_EMAIL = "EMAIL@EMAIL.EMAIL";
    private String ANY_PASSWORD = "LENGTHY_ANY_PASSWORD";
    private boolean NON_LOCKED = true;
    private User ANY_USER = new User(ANY_LONG, ANY_USERNAME, ANY_LASTNAME, ANY_EMAIL, ANY_PASSWORD, new ArrayList<>(), NON_LOCKED);

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetails = mock(UserDetails.class);
        SUT = new AdminService(
                userRepository,
                mock(SecurityLogService.class),
                mock(Map.class)
        );
    }

    @Test
    void shouldReturnListOfUsers() {
        // arrange
        User user1 = new User(1L, "USER_1", ANY_LASTNAME, "EMAIL1@EMAIL.EMAIL", ANY_PASSWORD, Collections.singletonList(Roles.ROLE_USER), NON_LOCKED);
        User user2 = new User(2L, "USER_2", ANY_LASTNAME, "EMAIL2@EMAIL.EMAIL", ANY_PASSWORD, Collections.singletonList(Roles.ROLE_ACCOUNTANT), NON_LOCKED);
        List<User> stub = List.of(user1, user2);
        when(userRepository.findAllByOrderByIdAsc()).thenReturn(stub);

        // act
        List<User> actual = SUT.getUsersList();

        // assert
        assertThat(actual).hasSize(2).containsExactly(
                new User(1L, "USER_1", new String(ANY_LASTNAME), "EMAIL1@EMAIL.EMAIL", new String(ANY_PASSWORD), Collections.singletonList(Roles.ROLE_USER), NON_LOCKED),
                new User(2L, "USER_2", new String(ANY_LASTNAME), "EMAIL2@EMAIL.EMAIL", new String(ANY_PASSWORD), Collections.singletonList(Roles.ROLE_ACCOUNTANT), NON_LOCKED)
        );
    }

    @Test
    void shouldDeleteUser() {
        // arrange
        User userToDelete = ANY_USER;
        userToDelete.addRole(Roles.ROLE_USER);
        when(userRepository.findByEmailIgnoreCase(ANY_EMAIL)).thenReturn(Optional.of(userToDelete));

        // act
        SUT.deleteUser(userDetails, ANY_EMAIL);

        // assert
        verify(userRepository, times(1)).delete(userToDelete);
    }

    @Test
    void shouldThrowExceptionOnDeletingNotFoundUser() {
        // arrange
        when(userRepository.findByEmailIgnoreCase(ANY_EMAIL)).thenReturn(Optional.empty());

        // act & assert
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> SUT.deleteUser(userDetails, ANY_EMAIL));
    }

    @Test
    void shouldThrowExceptionOnDeletingAdmin() {
        // arrange
        User adminToDelete = ANY_USER;
        adminToDelete.addRole(Roles.ROLE_ADMINISTRATOR);
        when(userRepository.findByEmailIgnoreCase(ANY_EMAIL)).thenReturn(Optional.of(adminToDelete));

        // act & assert
        assertThatExceptionOfType(AdminDeletionException.class)
                .isThrownBy(() -> SUT.deleteUser(userDetails, ANY_EMAIL));
    }

    @Test
    void shouldAddRoleToUser() {
        // arrange
        User userToModify = ANY_USER;
        userToModify.addRole(Roles.ROLE_USER);

        String newRole = "ACCOUNTANT";
        RoleDTO roleDTO = new RoleDTO(ANY_EMAIL, newRole, OperationType.GRANT);

        when(userRepository.findByEmailIgnoreCase(ANY_EMAIL)).thenReturn(Optional.of(userToModify));

        // act
        User actual = SUT.changeRole(userDetails, roleDTO);

        // assert
        assertThat(actual.getRoles()).contains(Roles.ROLE_ACCOUNTANT);
    }

    @Test
    void shouldRemoveRoleFromUser() {
        // arrange
        User userToModify = ANY_USER;
        userToModify.addRole(Roles.ROLE_USER);
        userToModify.addRole(Roles.ROLE_AUDITOR);

        String deleteRole = "AUDITOR";
        RoleDTO roleDTO = new RoleDTO(ANY_EMAIL, deleteRole, OperationType.REMOVE);
        when(userRepository.findByEmailIgnoreCase(ANY_EMAIL)).thenReturn(Optional.of(userToModify));

        // act
        User actual = SUT.changeRole(userDetails, roleDTO);

        // assert
        assertThat(actual.getRoles()).doesNotContain(Roles.ROLE_AUDITOR);
    }

    @Test
    void shouldNotLetRemoveRoleWhenCountIsOne() {
        // arrange
        User user = ANY_USER;
        user.addRole(Roles.ROLE_AUDITOR);

        String deleteRole = "AUDITOR";
        RoleDTO roleDTO = new RoleDTO(ANY_EMAIL, deleteRole, OperationType.REMOVE);
        when(userRepository.findByEmailIgnoreCase(ANY_EMAIL)).thenReturn(Optional.of(user));

        // act & assert
        assertThatExceptionOfType(TooLittleRolesException.class)
                .isThrownBy(() -> SUT.changeRole(userDetails, roleDTO));
    }

    @Test
    void shouldLockUser() {
        // arrange
        User user = ANY_USER;
        user.addRole(Roles.ROLE_USER);

        UserActionDTO userActionDTO = new UserActionDTO(user.getEmail(), AccountAction.LOCK);

        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));

        // act
        User actual = SUT.lockUnlockUser(userDetails, userActionDTO);

        // assert
        assertThat(actual.isAccountNonLocked()).isFalse();
    }

    @Test
    void shouldUnlockUser() {
        // arrange
        User user = ANY_USER;
        user.addRole(Roles.ROLE_USER);
        user.setAccountNonLocked(false);

        UserActionDTO userActionDTO = new UserActionDTO(user.getEmail(), AccountAction.UNLOCK);

        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));

        // act
        User actual = SUT.lockUnlockUser(userDetails, userActionDTO);

        // assert
        assertThat(actual.isAccountNonLocked()).isTrue();
    }

    @Test
    void shouldThrowExceptionOnAdminLocking() {
        // arrange
        User admin = ANY_USER;
        admin.addRole(Roles.ROLE_ADMINISTRATOR);

        UserActionDTO userActionDTO = new UserActionDTO(admin.getEmail(), AccountAction.LOCK);

        when(userRepository.findByEmailIgnoreCase(admin.getEmail())).thenReturn(Optional.of(admin));

        // act & assert
        assertThatExceptionOfType(LockAdminException.class)
                .isThrownBy(() -> SUT.lockUnlockUser(userDetails, userActionDTO));
    }
}