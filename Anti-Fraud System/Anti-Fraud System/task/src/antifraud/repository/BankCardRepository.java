package antifraud.repository;

import antifraud.model.BankCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BankCardRepository extends CrudRepository<BankCard, Long> {

    Optional<BankCard> findByNumber(String number);

    List<BankCard> findAllByOrderByIdAsc();
}
