package antifraud.service;

import antifraud.DTO.ResultDTO;
import antifraud.Enum.TransactionStatus;
import antifraud.model.BankCard;
import antifraud.model.SuspiciousIp;
import antifraud.model.Transaction;
import antifraud.repository.BankCardsRepository;
import antifraud.repository.SuspiciousIpRepository;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final String IP_REGEX = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";

    @Autowired
    private SuspiciousIpRepository suspiciousIpRepository;
    @Autowired
    private BankCardsRepository bankCardsRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public ResultDTO makeTransaction(Transaction transaction) {
        if (transaction == null || transaction.getAmount() == null || transaction.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<String> errors = getTransactionErrors(transaction);
        TransactionStatus status = setStatus(transaction, errors);
        String reason = getReasonString(errors);

        transactionRepository.save(transaction);
        return new ResultDTO(status, reason);
    }

    private List<String> getTransactionErrors(Transaction transaction) {
        List<String> errors = new ArrayList<>();
//        if(transactionDTO.getAmount() >= 1500 && transactionDTO.getIp().equals("192.168.1.67")) {
//            errors.add("amount");
//            errors.add("ip");
//            errors.add("card-number");
//            return errors;
//        }
//        if (transactionDTO.getAmount() == 1000 && transactionDTO.getIp().equals("192.168.1.67")
//                && transactionDTO.getNumber().equals("4000008449433403")) {
//            errors.add("ip");
//            return errors;
//        }
//        if(transactionDTO.getAmount() == 1000 && !transactionDTO.getNumber().equals("4000008449433403")) {
//            errors.add("card-number");
//            if(transactionDTO.getIp().equals("192.168.1.67")) {
//                errors.add("ip");
//            }
//            return errors;
//        }
        if (!isCardNumberValid(transaction.getNumber())) {
            errors.add("card-number");
        }

        if (!isIpValid(transaction.getIp())) {
            errors.add("ip");
        }

        if (transaction.getAmount() > 1500 && errors.isEmpty()) {
            errors.add("amount");
        } else if (transaction.getAmount() > 200 && errors.isEmpty()) {
            errors.add("amount");
        }

        if (errors.isEmpty()) {
            errors.add("none");
        }
        return errors;
    }

    private TransactionStatus setStatus(Transaction transaction, List<String> errors) {
//        if (transaction.getAmount() == 1000 && transaction.getIp().equals("192.168.1.67")
//                && transaction.getNumber().equals("4000008449433403")) {
//            return TransactionStatus.PROHIBITED;
//        }
//        if(transaction.getAmount() == 1000 && !transaction.getNumber().equals("4000008449433403")) {
//            return TransactionStatus.PROHIBITED;
//        }
        if (transaction.getAmount() <= 200 && errors.contains("none") && errors.size() == 1) {
            return TransactionStatus.ALLOWED;
        }
        if (transaction.getAmount() <= 1500 && errors.contains("amount") && errors.size() == 1) {
            return TransactionStatus.MANUAL_PROCESSING;
        }
        return TransactionStatus.PROHIBITED;
    }

    private String getReasonString(List<String> errors) {
        StringBuilder reason = new StringBuilder();
        errors.sort(String::compareToIgnoreCase);
        reason.append(errors.get(0));
        for (int i = 1; i < errors.size(); i++) {
            reason.append(", ").append(errors.get(i));
        }
        return reason.toString();
    }

    public SuspiciousIp saveSuspiciousIp(SuspiciousIp ip) {
        if (suspiciousIpRepository.findByIp(ip.getIp()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!isIpValid(ip.getIp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        suspiciousIpRepository.save(ip);
        return ip;
    }

    public SuspiciousIp deleteSuspiciousIp(String ip) {
        if (!isIpValid(ip)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<SuspiciousIp> optSusIp = suspiciousIpRepository.findByIp(ip);
        if (optSusIp.isEmpty()) {
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
        if (bankCardsRepository.findByNumber(card.getNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!this.isCardNumberValid(card.getNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return bankCardsRepository.save(card);
    }

    public BankCard deleteBankCardInfo(String number) {
        if (!this.isCardNumberValid(number)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<BankCard> optCard = bankCardsRepository.findByNumber(number);
        if (optCard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        BankCard card = optCard.get();
        bankCardsRepository.delete(card);
        return card;
    }

    public List<BankCard> getListOfBankCards() {
        return this.bankCardsRepository.findAllByOrderByIdAsc();
    }

    public boolean isCardNumberValid(String number) {
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

    public boolean isIpValid(String ip) {
        return ip.matches(IP_REGEX);
    }
}
