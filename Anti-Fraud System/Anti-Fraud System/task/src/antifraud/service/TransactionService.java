package antifraud.service;

import antifraud.DTO.ResultDTO;
import antifraud.model.SuspiciousIp;
import antifraud.model.Transaction;
import antifraud.repository.SuspiciousIpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransactionService {

    private final String IP_REGEX = "(\\d{1,3}\\.?\\b){4}";

    @Autowired
    private SuspiciousIpRepository suspiciousIpRepository;

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
}
