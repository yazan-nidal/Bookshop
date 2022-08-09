package exp.exalt.bookshop.exceptions.author_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuthorNullException extends RuntimeException {
    private static final long serialVersionUID = 11L;
    String message;
    private HttpStatus status;

    public AuthorNullException(String message,HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.status = httpStatus;
    }

    public AuthorNullException() {
        super();
        message = "";
        status = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
