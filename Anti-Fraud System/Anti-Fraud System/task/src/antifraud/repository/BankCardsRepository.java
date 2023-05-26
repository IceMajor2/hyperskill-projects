package antifraud.repository;

import antifraud.model.BankCard;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BankCardsRepository extends CrudRepository<BankCard, Long> {

    Optional<BankCard> findByNumber(Long number);
}
