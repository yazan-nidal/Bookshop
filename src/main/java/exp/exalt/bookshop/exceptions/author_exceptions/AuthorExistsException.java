package exp.exalt.bookshop.exceptions.author_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AuthorExistsException extends RuntimeException {
    private static final long serialVersionUID = 3L;
    final String message;
    private HttpStatus httpStatus;

    public AuthorExistsException(String message) {
        super(message);
        this.message = message;
        this.httpStatus = HttpStatus.CONFLICT;
    }
    
    public AuthorExistsException(String message,HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
