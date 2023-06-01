package account.repositories;

import account.models.SecurityLog;
import org.springframework.data.repository.CrudRepository;

public interface SecurityLogRepository extends CrudRepository<SecurityLog, Long> {
}
