package antifraud.service;

import antifraud.DTO.ResultDTO;
import antifraud.DTO.TransactionDTO;
import antifraud.Enum.TransactionStatus;
import antifraud.model.Transaction;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public ResultDTO makeTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO == null || transactionDTO.getAmount() == null || transactionDTO.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = null;
        try {
            transaction = new Transaction(transactionDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        transactionRepository.save(transaction);

//        if(transaction.getAmount() >= 1500 && transaction.getIp().equals("192.168.1.67")) {
//            String reason = getReasonString(new ArrayList<String>
//                    (Arrays.asList(new String[] {"amount","ip","card-number"})));
//            return new ResultDTO(TransactionStatus.PROHIBITED, reason);
//        }
//        if (transaction.getAmount() == 1000 && transaction.getIp().equals("192.168.1.67")
//                && transaction.getNumber().equals("4000008449433403")) {
//            String reason = getReasonString(new ArrayList<String>(Arrays.asList(new String[] {"ip"})));
//            return new ResultDTO(TransactionStatus.PROHIBITED, reason);
//        }
//        if(transaction.getAmount() == 1000 && !transaction.getNumber().equals("4000008449433403")) {
//            List<String> errs = new ArrayList<>();
//            errs.add("card-number");
//            if(transaction.getIp().equals("192.168.1.67")) {
//                errs.add("ip");
//            }
//            String reason = getReasonString(errs);
//            return new ResultDTO(TransactionStatus.PROHIBITED, reason);
//        }

        List<String> errors = getProhibitedErrors(transaction);
        if (!errors.isEmpty()) {
            String reason = getReasonString(errors);
            TransactionStatus status = TransactionStatus.PROHIBITED;
            return new ResultDTO(status, reason);
        }

        errors = getManualProcessingErrors(transaction);
        if (!errors.isEmpty()) {
            String reason = getReasonString(errors);
            TransactionStatus status = TransactionStatus.MANUAL_PROCESSING;
            return new ResultDTO(status, reason);
        }
        String reason = "none";
        TransactionStatus status = TransactionStatus.ALLOWED;
        return new ResultDTO(status, reason);
    }

    private List<String> getManualProcessingErrors(Transaction transaction) {
        List<String> errors = new ArrayList<>();
        String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(transaction.getDate());

        if (transactionRepository.numberOfDifferentRegionsInLastHourMinus(formattedDate,
                transaction.getRegion().toString()) == 2) {
            errors.add("region-correlation");
        }
        if (transactionRepository.numberOfDifferentIpsInLastHourMinus(formattedDate,
                transaction.getIp()) == 2) {
            errors.add("ip-correlation");
        }

        if (transaction.getAmount() > 200) {
            errors.add("amount");
        }

        return errors;
    }

    private List<String> getProhibitedErrors(Transaction transaction) {
        List<String> errors = new ArrayList<>();
        String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(transaction.getDate());

        if (transactionRepository.numberOfDifferentRegionsInLastHourMinus(formattedDate,
                transaction.getRegion().toString()) > 2) {
            errors.add("region-correlation");
        }
        if (transactionRepository.numberOfDifferentIpsInLastHourMinus(formattedDate,
                transaction.getIp()) > 2) {
            errors.add("ip-correlation");
        }
        if (transaction.getAmount() > 1500) {
            errors.add("amount");
        }
        if (!SuspiciousIpService.isIpValid(transaction.getIp())) {
            errors.add("ip");
        }
        if (!BankCardService.isCardNumberValid(transaction.getNumber())) {
            errors.add("card-number");
        }
        return errors;
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