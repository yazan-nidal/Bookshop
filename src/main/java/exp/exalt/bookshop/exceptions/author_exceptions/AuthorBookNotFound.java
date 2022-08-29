package exp.exalt.bookshop.exceptions.author_exceptions;

import org.springframework.http.HttpStatus;

public class AuthorBookNotFound extends RuntimeException{
    private static final long serialVersionUID = 11L;

    @Override
    public String getMessage() {
        return message;
    }

    private final String message;
    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public AuthorBookNotFound() {
        super();
        message = "";
        httpStatus = HttpStatus.NO_CONTENT;
    }

    public AuthorBookNotFound(String message,HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
