package platform.repositories;

import org.springframework.data.repository.CrudRepository;
import platform.models.Code;

import java.util.List;

public interface CodeRepository extends CrudRepository<Code, Long> {

    List<Code> findFirst10ByOrderByDateDesc();

    List<Code> findAll();

    boolean existsByCode(String code);
}
