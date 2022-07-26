package exp.exalt.bookshop.exceptions;

import exp.exalt.bookshop.exceptions.author_exceptions.AuthorExistsException;
import exp.exalt.bookshop.exceptions.author_exceptions.EmptyAuthorListException;
import exp.exalt.bookshop.exceptions.author_exceptions.AuthorNotFoundException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookAuthorNotNullException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookExistsException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookNotFoundException;
import exp.exalt.bookshop.exceptions.customer_exceptions.CustomerExistsException;
import exp.exalt.bookshop.exceptions.customer_exceptions.CustomerNotFoundException;
import exp.exalt.bookshop.exceptions.customer_exceptions.EmptyCustomerListException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler
{

    @ExceptionHandler(EmptyAuthorListException.class)
    public final ResponseEntity<Object> handleEmptyAuthorListException(EmptyAuthorListException ex, WebRequest request)
    {
        return new ResponseEntity<>(ex, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Object> handleAuthorNotFoundException(AuthorNotFoundException ex, WebRequest request) {
        CustomError customError = new CustomError(NOT_FOUND,ex.getMessage(),request.getDescription(false),ex);
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(AuthorExistsException.class)
    public ResponseEntity<Object> handleAuthorExistsException(AuthorExistsException ex, WebRequest request) {
        CustomError customError = new CustomError(CONFLICT,ex.getMessage(),request.getDescription(false),ex);
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(EmptyCustomerListException.class)
    public final ResponseEntity<Object> handleEmptyCustomerListException(EmptyCustomerListException ex, WebRequest request)
    {
        return new ResponseEntity<>(ex, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex, WebRequest request) {
        CustomError customError = new CustomError(NOT_FOUND,ex.getMessage(),request.getDescription(false),ex);
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(CustomerExistsException.class)
    public ResponseEntity<Object> handleCustomerExistsException(CustomerExistsException ex, WebRequest request) {
        CustomError customError = new CustomError(CONFLICT,ex.getMessage(),request.getDescription(false),ex);
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex, WebRequest request) {
        CustomError customError = new CustomError(NOT_FOUND,ex.getMessage(),request.getDescription(false),ex);
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(BookExistsException.class)
    public ResponseEntity<Object> handleBookExistsException(BookExistsException ex, WebRequest request) {
        CustomError customError = new CustomError(CONFLICT,ex.getMessage(),request.getDescription(false),ex);
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(BookAuthorNotNullException.class)
    public ResponseEntity<Object> handleBookAuthorNotNullException(BookAuthorNotNullException ex, WebRequest request) {
        CustomError customError = new CustomError(FAILED_DEPENDENCY,ex.getMessage(),request.getDescription(false),ex);
        return buildResponseEntity(customError);
    }


    private ResponseEntity<Object> buildResponseEntity(CustomError error) {
        return new ResponseEntity<>(error, error.getStatus());
    }
}
