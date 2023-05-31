package account.services;

import account.DTO.PaymentDTO;
import account.exceptions.NoSuchPaymentException;
import account.exceptions.PaymentMadeForPeriodException;
import account.exceptions.UserNotExistsException;
import account.models.Payment;
import account.models.User;
import account.repositories.PaymentRepository;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusinessService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;

    public User getPayrolls(UserDetails userDetails) {
        var user = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if(user.isEmpty()) {
            throw new UserNotExistsException();
        }
        return user.get();
    }

    @Transactional
    public void uploadPayrolls(List<PaymentDTO> paymentDTOS) {
        for (PaymentDTO paymentDTO : paymentDTOS) {
            User user = userRepository.findByEmailIgnoreCase(paymentDTO.getEmail())
                    .orElseThrow(() -> new UserNotExistsException());
            Payment payment = new Payment(paymentDTO, user);

            if(!isPaymentUnique(payment)) {
                throw new PaymentMadeForPeriodException();
            }

            paymentRepository.save(payment);
        }
    }

    public void updatePayment(PaymentDTO paymentDTO) {
        User user = userRepository.findByEmailIgnoreCase(paymentDTO.getEmail())
                .orElseThrow(() -> new UserNotExistsException());
        Payment dbPayment = paymentRepository.findByUserIdAndPeriod(user.getId(), paymentDTO.getPeriod())
                .orElseThrow(() -> new NoSuchPaymentException());
        dbPayment.setSalary(paymentDTO.getSalary());
        paymentRepository.save(dbPayment);
    }

    private boolean isPaymentUnique(Payment payment) {
        User user = payment.getUser();
        List<Payment> payments = paymentRepository.findByUserId(user.getId());
        for(Payment paymentObj : payments) {
            if(paymentObj.getPeriod().equals(payment.getPeriod())) {
                return false;
            }
        }
        return true;
    }
}
