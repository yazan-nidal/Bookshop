package exp.exalt.bookshop.exceptions.author_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class AuthorNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 2L;
    final String message;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private HttpStatus httpStatus;

    public AuthorNotFoundException() {
        super();
        this.httpStatus = HttpStatus.NO_CONTENT;
        message = "";
    }
    public AuthorNotFoundException(String message) {
        super(message);
        this.message = message;
    }
    public AuthorNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }


}
