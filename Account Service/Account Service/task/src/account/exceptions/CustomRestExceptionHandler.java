package account.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException ex,
             HttpHeaders headers,
             HttpStatusCode status,
             WebRequest request) {

        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, errors.get(0), path);
        return handleExceptionInternal
                (ex, apiError, headers, ex.getStatusCode(), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = getMessage(exception.getConstraintViolations());
        String path = getPath(exception.getConstraintViolations().iterator().next());

        ApiError error = new ApiError(status, message, path);
        return ResponseEntity.badRequest().body(error);
    }

    private String getMessage(Set<ConstraintViolation<?>> violations) {
        StringBuilder message = new StringBuilder("");
        var excIterator = violations.stream().iterator();
        while(excIterator.hasNext()) {
            var violation = excIterator.next();
            message.append(violation.getMessage()).append("; ");
        }
        return message.delete(message.length() - 2, message.length()).toString();
    }

    private String getPath(ConstraintViolation violation) {
        StringBuilder path = new StringBuilder("/");

        for(Path.Node node : violation.getPropertyPath()) {
            path.append(node).append('/');
        }
        return path.deleteCharAt(path.length() - 1).toString();
    }
}