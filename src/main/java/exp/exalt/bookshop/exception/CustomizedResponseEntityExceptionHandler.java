package exp.exalt.bookshop.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{

    @ExceptionHandler(Exception.class)
//override method of ResponseEntityExceptionHandler class
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)
    {
//creating exception response structure
        ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
//returning exception structure and specific status
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new CustomError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        CustomError customError = new CustomError(NOT_FOUND);
        customError.setMessage(ex.getMessage());
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(EmptyEntityException.class)
    public ResponseEntity<Object> handleEmptyEntity(EmptyEntityException ex) {
        CustomError customError = new CustomError(NO_CONTENT);
        customError.setMessage(ex.getMessage());
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Object> handleEntityExists(EntityExistsException ex) {
        CustomError customError = new CustomError(CONFLICT);
        customError.setMessage(ex.getMessage());
        return buildResponseEntity(customError);
    }

    private ResponseEntity<Object> buildResponseEntity(CustomError error) {
        return new ResponseEntity<>(error, error.getStatus());
    }
}
