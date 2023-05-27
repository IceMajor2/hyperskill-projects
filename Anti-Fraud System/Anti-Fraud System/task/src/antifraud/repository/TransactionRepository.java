package antifraud.repository;

import antifraud.Enum.Region;
import antifraud.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query(value = "SELECT COUNT(DISTINCT region) FROM transactions " +
            "WHERE date >= DATEADD(HOUR, -1, GETDATE()) " +
            "AND region <> ?1", nativeQuery = true)
    int numberOfDifferentRegionsInLastHourMinus(Region region);

    @Query(value = "SELECT COUNT(DISTINCT ip) FROM transactions " +
            "WHERE date >= DATEADD(HOUR, -1, GETDATE()) " +
            "AND ip <> ?1", nativeQuery = true)
    int numberOfDifferentIpsInLastHourMinus(String ip);
}
