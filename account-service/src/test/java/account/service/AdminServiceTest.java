package account.service;

import account.enumerated.Roles;
import account.model.User;
import account.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    private AdminService SUT;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        SUT = new AdminService(
                userRepository,
                mock(SecurityLogService.class),
                mock(Map.class)
        );
    }

    @Test
    void shouldReturnListOfUsers() {
        // arrange
        User user1 = new User(1L, "USER_1", "LASTNAME", "EMAIL@EMAIL.EMAIL", "LENGTHY_PASSWORD", Collections.singletonList(Roles.ROLE_USER), true);
        User user2 = new User(2L, "USER_2", "LASTNAME", "EMAIL@EMAIL.EMAIL", "LENGTHY_PASSWORD", Collections.singletonList(Roles.ROLE_ACCOUNTANT), true);
        List<User> stub = List.of(user1, user2);
        when(userRepository.findAllByOrderByIdAsc()).thenReturn(stub);

        // act
        List<User> actual = SUT.getUsersList();

        // assert
        assertThat(actual).hasSize(2).containsExactly(
                new User(1L, "USER_1", "LASTNAME", "EMAIL@EMAIL.EMAIL", "LENGTHY_PASSWORD", Collections.singletonList(Roles.ROLE_USER), true),
                new User(2L, "USER_2", "LASTNAME", "EMAIL@EMAIL.EMAIL", "LENGTHY_PASSWORD", Collections.singletonList(Roles.ROLE_ACCOUNTANT), true)
        );
    }

    @Test
    void shouldDeleteUser() {
        // arrange
        String email = "EMAIL@EMAIL.EMAIL";
        UserDetails details = mock(UserDetails.class);
        User userToDelete = new User(1L, "USER_1", "LASTNAME", email, "LENGTHY_PASSWORD", Collections.singletonList(Roles.ROLE_USER), true);
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(userToDelete));

        // act
        SUT.deleteUser(details, email);

        // assert
        verify(userRepository, times(1)).delete(userToDelete);
    }

    @Test
    void shouldThrowExceptionOnDeletingAdmin() {

    }

    @Test
    void changeRole() {
    }

    @Test
    void lockUnlockUser() {
    }
}