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
import java.util.*;

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
    void shouldReturnAllSalariesForUser() {
        // arrange
        User user = new User(1L, "ANY_NAME", "ANY_LAST_NAME", "email@email.com", "LENGTHY_PASSWORD", new ArrayList<>(List.of(Roles.ROLE_USER)), true);
        List<Payment> payments = new ArrayList<>(List.of(
                new Payment(4L, user, new Calendar.Builder().set(Calendar.YEAR, 2023).set(Calendar.MONTH, Calendar.APRIL).build().getTime(), 120000L),
                new Payment(3L, user, new Calendar.Builder().set(Calendar.YEAR, 2023).set(Calendar.MONTH, Calendar.FEBRUARY).build().getTime(), 392500L),
                new Payment(2L, user, new Calendar.Builder().set(Calendar.YEAR, 2023).set(Calendar.MONTH, Calendar.JANUARY).build().getTime(), 410535L),
                new Payment(1L, user, new Calendar.Builder().set(Calendar.YEAR, 2022).set(Calendar.MONTH, Calendar.DECEMBER).build().getTime(), 190090L)
        ));

        when(userDetails.getUsername()).thenReturn("email@email.com");
        when(userRepository.findByEmailIgnoreCase("email@email.com")).thenReturn(Optional.of(user));
        when(paymentRepository.findByUserIdOrderByPeriodDesc(user.getId())).thenReturn(payments);

        // act
        List<AuthPaymentDTO> actual = SUT.getPayrolls(userDetails);

        // assert
        assertThat(actual).hasSize(4);
        assertThat(actual).containsExactly(
                new AuthPaymentDTO("ANY_NAME", "ANY_LAST_NAME", new Calendar.Builder().set(Calendar.YEAR, 2023).set(Calendar.MONTH, Calendar.APRIL).build().getTime(), 120000L),
                new AuthPaymentDTO("ANY_NAME", "ANY_LAST_NAME", new Calendar.Builder().set(Calendar.YEAR, 2023).set(Calendar.MONTH, Calendar.FEBRUARY).build().getTime(), 392500L),
                new AuthPaymentDTO("ANY_NAME", "ANY_LAST_NAME", new Calendar.Builder().set(Calendar.YEAR, 2023).set(Calendar.MONTH, Calendar.JANUARY).build().getTime(), 410535L),
                new AuthPaymentDTO("ANY_NAME", "ANY_LAST_NAME", new Calendar.Builder().set(Calendar.YEAR, 2022).set(Calendar.MONTH, Calendar.DECEMBER).build().getTime(), 190090L));
    }

    @Test
    void uploadPayrolls() {
    }

    @Test
    void updatePayment() {
    }
}