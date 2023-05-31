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
        String message = "Body is invalid";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = getPath(exception);

        ApiError error = new ApiError(status, message, path);
        return ResponseEntity.badRequest().body(error);
    }

    private String getPath(ConstraintViolationException exception) {
        String path = null;
        ConstraintViolation<?> violation = exception.getConstraintViolations()
                .iterator().next();
        // last node is path
        for(Path.Node node : violation.getPropertyPath()) {
            path = node.getName();
        }
        return path;
    }
}