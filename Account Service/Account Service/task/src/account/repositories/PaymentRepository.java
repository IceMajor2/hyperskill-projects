package account.repositories;

import account.models.Payment;
import account.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);
}
