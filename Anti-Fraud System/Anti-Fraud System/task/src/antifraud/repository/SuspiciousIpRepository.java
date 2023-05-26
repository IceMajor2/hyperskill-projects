package antifraud.repository;

import antifraud.model.SuspiciousIp;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SuspiciousIpRepository extends CrudRepository<SuspiciousIp, Long> {

    Optional<SuspiciousIp> findByIp(String ip);

    List<SuspiciousIp> findAllByOrderByIdAsc();
}