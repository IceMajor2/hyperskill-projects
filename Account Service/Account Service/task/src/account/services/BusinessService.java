package account.services;

import account.DTO.PaymentDTO;
import account.exceptions.PaymentMadeForPeriod;
import account.exceptions.UserNotExistsException;
import account.models.Payment;
import account.models.User;
import account.repositories.PaymentRepository;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void uploadPayrolls(List<PaymentDTO> paymentDTOS) {
        for (PaymentDTO paymentDTO : paymentDTOS) {
            User user = userRepository.findByEmailIgnoreCase(paymentDTO.getEmail())
                    .orElseThrow(() -> new UserNotExistsException());
            Payment payment = new Payment(paymentDTO, user);

            if(!isPaymentUnique(payment)) {
                throw new PaymentMadeForPeriod();
            }

            paymentRepository.save(payment);
        }
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
