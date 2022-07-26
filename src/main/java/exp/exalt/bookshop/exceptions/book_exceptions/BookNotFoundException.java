package exp.exalt.bookshop.exceptions.book_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 9L;
    final String message;

    public BookNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
