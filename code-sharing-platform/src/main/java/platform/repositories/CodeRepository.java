package platform.repositories;

import org.springframework.data.repository.CrudRepository;
import platform.models.Code;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends CrudRepository<Code, String> {

    List<Code> findFirst10ByOrderByDateDesc();

    List<Code> findAll();

    Optional<Code> findByNumId(Long numId);

    boolean existsByCode(String code);
}
