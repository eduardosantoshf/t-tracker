package deliveries_engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ErrorWarning extends Exception{

    private static final long serialVersionUID = 1L;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> ErrorWarning(Exception ex, WebRequest webRequest) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }


}
