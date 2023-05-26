package antifraud.service;

import antifraud.DTO.ResultDTO;
import antifraud.model.BankCard;
import antifraud.model.SuspiciousIp;
import antifraud.model.Transaction;
import antifraud.repository.BankCardsRepository;
import antifraud.repository.SuspiciousIpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final String IP_REGEX = "(\\d{1,3}\\.?\\b){4}";

    @Autowired
    private SuspiciousIpRepository suspiciousIpRepository;
    @Autowired
    private BankCardsRepository bankCardsRepository;

    public ResultDTO makeTransaction(Transaction transaction) {
        if(transaction == null || transaction.getAmount() == null || transaction.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(transaction.getAmount() > 1500) {
            return new ResultDTO("PROHIBITED");
        }
        if(transaction.getAmount() > 200) {
            return new ResultDTO("MANUAL_PROCESSING");
        }
        return new ResultDTO("ALLOWED");
    }

    public SuspiciousIp saveSuspiciousIp(SuspiciousIp ip) {
        if(suspiciousIpRepository.findByIp(ip.getIp()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if(!ip.getIp().matches(IP_REGEX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        suspiciousIpRepository.save(ip);
        return ip;
    }

    public SuspiciousIp deleteSuspiciousIp(String ip) {
        if(!ip.matches(IP_REGEX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<SuspiciousIp> optSusIp = suspiciousIpRepository.findByIp(ip);
        if(optSusIp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SuspiciousIp susIp = optSusIp.get();
        suspiciousIpRepository.delete(susIp);
        return susIp;
    }

    public List<SuspiciousIp> listOfSuspiciousIps() {
        return suspiciousIpRepository.findAllByOrderByIdAsc();
    }

    public BankCard saveBankCardInfo(BankCard card) {
        if(bankCardsRepository.findByNumber(card.getNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if(!this.isCardNumberValid(card.getNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return bankCardsRepository.save(card);
    }

    public boolean isCardNumberValid(Long number) {
        char[] numArr = number.toString().toCharArray();
        int sum = 0;
        int parity = numArr.length % 2;
        for(int i = 0; i < numArr.length; i++) {
            int digit = numArr[i] - 48;
            if(i % 2 != parity) {
                sum = sum + digit;
            } else if(digit > 4) {
                sum = sum + 2 * digit - 9;
            } else {
                sum = sum + 2 * digit;
            }
        }
        return sum % 10 == 0;
    }
}
