package exp.exalt.bookshop.exceptions.author_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class AuthorBookNull extends RuntimeException {
    private static final long serialVersionUID = 11L;
    final String message;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    HttpStatus httpStatus;

    public  AuthorBookNull() {
        message = "";
        httpStatus= HttpStatus.NO_CONTENT;
    }
    public AuthorBookNull(String message,HttpStatus httpStatus) {
        super();
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
