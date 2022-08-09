package exp.exalt.bookshop.exceptions.book_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookExistsException extends RuntimeException {
    private static final long serialVersionUID = 7L;
    final String message;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    HttpStatus httpStatus;

    public BookExistsException(String message) {
        super(message);
        this.message = message;
        httpStatus = HttpStatus.CONFLICT;
    }

    public BookExistsException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
