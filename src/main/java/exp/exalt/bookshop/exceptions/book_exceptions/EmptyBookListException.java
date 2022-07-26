package exp.exalt.bookshop.exceptions.book_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class EmptyBookListException extends RuntimeException {
    private static final long serialVersionUID = 8L;

    public EmptyBookListException() {
        super();
    }
}
