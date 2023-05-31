package account.repositories;

import account.models.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);

    Optional<Payment> findByUserIdAndPeriod(Long userId, Date period);
}
