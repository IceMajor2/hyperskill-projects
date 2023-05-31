package account.repositories;

import account.models.Payment;
import account.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    List<User> findAllByOrderByIdAsc();
}
