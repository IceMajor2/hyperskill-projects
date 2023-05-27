package antifraud.repository;

import antifraud.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query(value = "SELECT COUNT(DISTINCT region) FROM transactions " +
            "WHERE date >= (TIMESTAMPADD(HOUR, -1, CURRENT_DATE)) " +
            "AND region <> ?1", nativeQuery = true)
    int numberOfDifferentRegionsInLastHourMinus(String region);

    @Query(value = "SELECT COUNT(DISTINCT ip) FROM transactions " +
            "WHERE date >= (TIMESTAMPADD(HOUR, -1, CURRENT_DATE)) " +
            "AND ip <> ?1", nativeQuery = true)
    int numberOfDifferentIpsInLastHourMinus(String ip);
}
