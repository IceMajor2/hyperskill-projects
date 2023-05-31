package account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Payment for that period was already made!")
public class PaymentMadeForPeriodException extends RuntimeException {
}
