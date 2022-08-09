package exp.exalt.bookshop.exceptions;

import exp.exalt.bookshop.exceptions.author_exceptions.*;
import exp.exalt.bookshop.exceptions.book_exceptions.BookAuthorNotNullException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookExistsException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookNotFoundException;
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
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(GeneralException ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getHttpStatus(),ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }


    @ExceptionHandler(AuthorGeneralException.class)
    public ResponseEntity<Object> handleAuthorGeneralException(AuthorGeneralException ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getHttpStatus(),ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(AuthorNullException.class)
    public ResponseEntity<Object> handleAuthorNullException(AuthorNullException ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getStatus(),ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(AuthorExistsException.class)
    public ResponseEntity<Object> handleAuthorExistsException(AuthorExistsException ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getHttpStatus(),ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(AuthorBookNull.class)
    public ResponseEntity<Object> handleAuthorBookNullException(AuthorBookNull ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getHttpStatus(),ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }


    @ExceptionHandler(BookExistsException.class)
    public ResponseEntity<Object> handleBookExistsException(BookExistsException ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getHttpStatus(), ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }



//Author Exception




    @ExceptionHandler(EmptyAuthorListException.class)
    public final ResponseEntity<Object> handleEmptyAuthorListException(EmptyAuthorListException ex, WebRequest request)
    {
        return new ResponseEntity<>(ex, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Object> handleAuthorNotFoundException(AuthorNotFoundException ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getHttpStatus(),ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }



    @ExceptionHandler(AuthorBookNotFound.class)
    public ResponseEntity<Object> handleAuthorBookNotFound(AuthorBookNotFound ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getHttpStatus(),ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex, WebRequest request) {
        CustomError customError = new CustomError(NOT_FOUND,ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }


    @ExceptionHandler(BookAuthorNotNullException.class)
    public ResponseEntity<Object> handleBookAuthorNotNullException(BookAuthorNotNullException ex, WebRequest request) {
        CustomError customError = new CustomError(FAILED_DEPENDENCY,ex.getMessage(),request.getDescription(false));
        return buildResponseEntity(customError);
    }

    private ResponseEntity<Object> buildResponseEntity(CustomError error) {
        return new ResponseEntity<>(error, error.getStatus());
    }
}
