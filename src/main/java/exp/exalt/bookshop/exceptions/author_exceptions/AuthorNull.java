package exp.exalt.bookshop.exceptions.author_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class AuthorNull extends RuntimeException {
    private static final long serialVersionUID = 11L;

    public AuthorNull() {
        super();
    }
}
