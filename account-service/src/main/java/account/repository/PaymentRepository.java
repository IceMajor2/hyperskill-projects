package account.repository;

import account.model.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);

    List<Payment> findByUserIdOrderByPeriodDesc(Long userId);

    Optional<Payment> findByUserIdAndPeriod(Long userId, Date period);
}
