package account.repository;

import account.model.BreachedPassword;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BreachedPasswordsRepository extends CrudRepository<BreachedPassword, Long> {

    Optional<BreachedPassword> findByPassword(String password);
}
