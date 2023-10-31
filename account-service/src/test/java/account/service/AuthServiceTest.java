package account.service;

import account.repository.BreachedPasswordsRepository;
import account.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;

class AuthServiceTest {

    private AuthService SUT;

    private UserRepository userRepository;
    private BreachedPasswordsRepository breachedPasswordsRepository;
    private PasswordEncoder passwordEncoder;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        breachedPasswordsRepository = mock(BreachedPasswordsRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userDetails = mock(UserDetails.class);

        SUT = new AuthService(
                mock(SecurityLogService.class),
                userRepository,
                breachedPasswordsRepository,
                passwordEncoder
        );
    }

    @Test
    void shouldRegisterUser() {
    }

    @Test
    void shouldChangePassword() {
    }
}