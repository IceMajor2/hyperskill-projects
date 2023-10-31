package account.service;

import account.dto.AuthPaymentDTO;
import account.enumerated.Roles;
import account.model.Payment;
import account.model.User;
import account.repository.PaymentRepository;
import account.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BusinessServiceTest {

    private BusinessService SUT;

    private PaymentRepository paymentRepository;
    private UserRepository userRepository;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        userRepository = mock(UserRepository.class);
        SUT = new BusinessService(paymentRepository, userRepository);
        userDetails = mock(UserDetails.class);
    }

    @Test
    void shouldReturnPayrollsOfPeriod() throws Exception {
        // arrange
        String period = "12-2022";
        Date date = new SimpleDateFormat("MM-yyyy").parse(period);
        User user = new User(1L, "ANY_NAME", "ANY_LAST_NAME", "email@email.com", "LENGTHY_PASSWORD", new ArrayList<>(List.of(Roles.ROLE_USER)), true);
        Payment payment = new Payment(1L, user, date, 300050L);

        when(userDetails.getUsername()).thenReturn("email@email.com");
        when(userRepository.findByEmailIgnoreCase("email@email.com")).thenReturn(Optional.of(user));
        when(paymentRepository.findByUserIdAndPeriod(user.getId(), date)).thenReturn(Optional.of(payment));

        // act
        AuthPaymentDTO actual = SUT.getPayrolls(userDetails, period);

        // assert
        assertThat(actual.getPeriod()).isEqualTo("December-2022");
        assertThat(actual.getSalary()).isEqualTo("3000 dollar(s) 50 cent(s)");
        assertThat(actual.getName()).isEqualTo("ANY_NAME");
    }

    @Test
    void testGetPayrolls() {
    }

    @Test
    void uploadPayrolls() {
    }

    @Test
    void updatePayment() {
    }
}