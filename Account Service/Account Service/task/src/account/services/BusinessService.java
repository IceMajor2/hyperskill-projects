package account.services;

import account.DTO.AuthPaymentDTO;
import account.DTO.PaymentDTO;
import account.exceptions.business.NoSuchPaymentException;
import account.exceptions.business.PaymentMadeForPeriodException;
import account.exceptions.auth.UserNotExistsException;
import account.models.Payment;
import account.models.User;
import account.repositories.PaymentRepository;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BusinessService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;

    public AuthPaymentDTO getPayrolls(UserDetails userDetails, String period) {
        try {
            Date date = new SimpleDateFormat("MM-yyyy").parse(period);
            User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).get();
            Payment payment = paymentRepository.findByUserIdAndPeriod(user.getId(), date)
                    .orElseThrow(() -> new NoSuchPaymentException());
            return new AuthPaymentDTO(user.getName(), user.getLastName(),
                    payment.getPeriod(), payment.getSalary());
        } catch (ParseException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<AuthPaymentDTO> getPayrolls(UserDetails userDetails) {
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).get();
        List<Payment> payments = paymentRepository.findByUserIdOrderByPeriodDesc(user.getId());
        return convertPaymentListToDTOList(payments);
    }

    @Transactional
    public void uploadPayrolls(List<PaymentDTO> paymentDTOS) {
        for (PaymentDTO paymentDTO : paymentDTOS) {
            User user = userRepository.findByEmailIgnoreCase(paymentDTO.getEmail())
                    .orElseThrow(() -> new UserNotExistsException());

            Payment payment = null;
            try {
                payment = new Payment(paymentDTO, user);
            } catch (ParseException exception) {
                throw new RuntimeException(exception);
            }

            if (!isPaymentUnique(payment)) {
                throw new PaymentMadeForPeriodException();
            }

            paymentRepository.save(payment);
        }
    }

    public void updatePayment(PaymentDTO paymentDTO) {
        User user = userRepository.findByEmailIgnoreCase(paymentDTO.getEmail())
                .orElseThrow(() -> new UserNotExistsException());
        try {
            Date date = new SimpleDateFormat("MM-yyyy").parse(paymentDTO.getPeriod());
            Payment dbPayment = paymentRepository.findByUserIdAndPeriod(user.getId(), date)
                    .orElseThrow(() -> new NoSuchPaymentException());
            dbPayment.setSalary(paymentDTO.getSalary());
            paymentRepository.save(dbPayment);
        } catch (ParseException exception) {
            throw new RuntimeException(exception);
        }
    }

    private boolean isPaymentUnique(Payment payment) {
        User user = payment.getUser();
        List<Payment> payments = paymentRepository.findByUserId(user.getId());
        for (Payment paymentObj : payments) {
            if (paymentObj.getPeriod().equals(payment.getPeriod())) {
                return false;
            }
        }
        return true;
    }

    private List<AuthPaymentDTO> convertPaymentListToDTOList(List<Payment> payments) {
        List<AuthPaymentDTO> authPaymentDTOS = new ArrayList<>();
        for (Payment payment : payments) {
            AuthPaymentDTO authPaymentDTO = new AuthPaymentDTO(payment);
            authPaymentDTOS.add(authPaymentDTO);
        }
        return authPaymentDTOS;
    }
}
