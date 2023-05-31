package account.services;

import account.DTO.PaymentDTO;
import account.exceptions.UserNotExistsException;
import account.models.User;
import account.repositories.PaymentRepository;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    public void uploadPayroll(List<PaymentDTO> paymentDTOS) {
        for(PaymentDTO paymentDTO : paymentDTOS) {
            if(!userExists(paymentDTO.getEmail())) {
                throw new UserNotExistsException();
            }


        }
    }

    public boolean userExists(String email) {
            return userRepository.findByEmailIgnoreCase(email).isPresent();
    }

    private boolean isPaymentUnique() {
        return true;
    }
}
