package platform.repositories;

import org.springframework.data.repository.CrudRepository;
import platform.models.Code;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodeRepository extends CrudRepository<Code, UUID> {

    List<Code> findAll();

    List<Code> findFirst10ByToBeTimeRestrictedFalseAndToBeViewRestrictedFalseOrderByDateDesc();

    Optional<Code> findByNumId(Long numId);

    boolean existsByCode(String code);
}
