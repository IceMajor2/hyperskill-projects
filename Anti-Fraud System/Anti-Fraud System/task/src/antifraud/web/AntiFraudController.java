package antifraud.web;

import antifraud.DTO.ResultDTO;
import antifraud.model.Transaction;
import antifraud.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/antifraud")
public class AntiFraudController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = {"/transaction", "/transaction/"}, method = RequestMethod.POST)
    // made 2 endpoints with and w/o trailing slash at the end
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    public ResponseEntity makeTransaction(@RequestBody Transaction transaction) {
        try {
            ResultDTO result = transactionService.makeTransaction(transaction);
            return new ResponseEntity(Map.of("result", result.getResult()), HttpStatus.OK);
        } catch (ResponseStatusException exception) {
            return new ResponseEntity(exception.getStatusCode());
        }
    }
}