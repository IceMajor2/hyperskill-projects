package antifraud.service;

import antifraud.model.BankCard;
import antifraud.repository.BankCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BankCardService {

    @Autowired
    private BankCardRepository bankCardRepository;

    public BankCard saveBankCardInfo(BankCard card) {
        if (bankCardRepository.findByNumber(card.getNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!this.isCardNumberValid(card.getNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return bankCardRepository.save(card);
    }

    public BankCard deleteBankCardInfo(String number) {
        if (!this.isCardNumberValid(number)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<BankCard> optCard = bankCardRepository.findByNumber(number);
        if (optCard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        BankCard card = optCard.get();
        bankCardRepository.delete(card);
        return card;
    }

    public List<BankCard> getListOfBankCards() {
        return this.bankCardRepository.findAllByOrderByIdAsc();
    }

    public static boolean isCardNumberValid(String number) {
        char[] numArr = number.toCharArray();
        int sum = 0;
        int parity = numArr.length % 2;
        for (int i = 0; i < numArr.length; i++) {
            int digit = numArr[i] - 48;
            if (i % 2 != parity) {
                sum = sum + digit;
            } else if (digit > 4) {
                sum = sum + 2 * digit - 9;
            } else {
                sum = sum + 2 * digit;
            }
        }
        return sum % 10 == 0;
    }
}
