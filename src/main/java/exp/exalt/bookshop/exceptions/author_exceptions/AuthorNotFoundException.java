package exp.exalt.bookshop.exceptions.author_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AuthorNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 2L;
    final String message;

    public AuthorNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
