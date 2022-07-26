package exp.exalt.bookshop.exceptions.book_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookExistsException extends RuntimeException {
    private static final long serialVersionUID = 7L;
    final String message;

    public BookExistsException(String message) {
        super(message);
        this.message = message;
    }
}
