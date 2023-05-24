package antifraud.repository;

import antifraud.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);

    List<User> findAllByOrderByIdAsc();
}
