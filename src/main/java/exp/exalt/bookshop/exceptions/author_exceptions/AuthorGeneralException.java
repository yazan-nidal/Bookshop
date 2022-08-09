package exp.exalt.bookshop.exceptions.author_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class AuthorGeneralException extends RuntimeException{
    private static final long serialVersionUID = 1411414L;
    final String message;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    HttpStatus httpStatus;

    public AuthorGeneralException(String message) {
        super(message);
        this.message = message;
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    public AuthorGeneralException(String message,HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
