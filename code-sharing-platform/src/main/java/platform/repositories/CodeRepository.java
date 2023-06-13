package platform.repositories;

import org.springframework.data.repository.CrudRepository;
import platform.models.Code;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodeRepository extends CrudRepository<Code, UUID> {

    List<Code> findFirst10ByOrderByDateDesc();

    List<Code> findAll();

    Optional<Code> findByNumId(Long numId);

    boolean existsByCode(String code);
}
