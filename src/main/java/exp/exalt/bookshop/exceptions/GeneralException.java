package exp.exalt.bookshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class GeneralException extends RuntimeException{
    private static final long serialVersionUID = 1411414L;
    final String message;
    private HttpStatus httpStatus;

    public GeneralException(String message) {
        super(message);
        this.message = message;
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GeneralException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
