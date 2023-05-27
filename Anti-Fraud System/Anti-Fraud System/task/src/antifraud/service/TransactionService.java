package antifraud.service;

import antifraud.DTO.ResultDTO;
import antifraud.Enum.TransactionStatus;
import antifraud.model.Transaction;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {


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
        if(transactionRepository.numberOfDifferentRegionsInLastHourMinus(transaction.getRegion()) >= 2) {
            errors.add("region-correlation");
        }

        if(transactionRepository.numberOfDifferentIpsInLastHourMinus(transaction.getIp()) >= 2) {
            errors.add("ip-correlation");
        }

        if (!BankCardService.isCardNumberValid(transaction.getNumber())) {
            errors.add("card-number");
        }

        if (!SuspiciousIpService.isIpValid(transaction.getIp())) {
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
}
