package account.repositories;

import account.models.BreachedPassword;
import org.springframework.data.repository.CrudRepository;

public interface BreachedPasswordsRepository extends CrudRepository<BreachedPassword, Long> {
}
