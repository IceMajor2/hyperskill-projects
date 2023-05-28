package antifraud.repository;

import antifraud.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query(value = "SELECT COUNT(DISTINCT region) FROM transactions " +
            "WHERE " +
            "date BETWEEN (TIMESTAMPADD(HOUR, -1, PARSEDATETIME(:date, 'yyyy-MM-dd HH:mm:ss'))) AND PARSEDATETIME(:date, 'yyyy-MM-dd HH:mm:ss') " +
            "AND region <> :region", nativeQuery = true)
    int numberOfDifferentRegionsInLastHourMinus(@Param("date") String date, @Param("region") String region);

    @Query(value = "SELECT COUNT(DISTINCT ip) FROM transactions " +
            "WHERE " +
            "date BETWEEN (TIMESTAMPADD(HOUR, -1, PARSEDATETIME(:date, 'yyyy-MM-dd HH:mm:ss'))) AND PARSEDATETIME(:date, 'yyyy-MM-dd HH:mm:ss') " +
            "AND ip <> :ip", nativeQuery = true)
    int numberOfDifferentIpsInLastHourMinus(@Param("date") String date, @Param("ip") String ip);
}
