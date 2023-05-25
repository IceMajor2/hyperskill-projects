package antifraud.service;

import antifraud.DTO.ResultDTO;
import antifraud.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransactionService {

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

}
