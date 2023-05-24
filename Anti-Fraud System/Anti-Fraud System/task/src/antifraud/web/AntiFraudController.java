package antifraud.web;

import antifraud.model.Transaction;
import antifraud.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AntiFraudController {

    public AntiFraudController() {
    }

    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity makeTransaction(@RequestBody Transaction transaction) {
        if (transaction == null || transaction.getAmount() == null || transaction.getAmount() <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (transaction.getAmount() > 1500) {
            return new ResponseEntity<>(Map.of("result", "PROHIBITED"), HttpStatus.OK);
        }
        if (transaction.getAmount() > 200) {
            return new ResponseEntity<>(Map.of("result", "MANUAL_PROCESSING"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("result", "ALLOWED"), HttpStatus.OK);
    }

    @PostMapping("/api/auth/user")
    public void createUser(@RequestBody User user) {

    }
}