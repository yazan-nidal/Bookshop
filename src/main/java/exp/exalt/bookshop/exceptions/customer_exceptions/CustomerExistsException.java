package exp.exalt.bookshop.exceptions.customer_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CustomerExistsException extends RuntimeException {
    private static final long serialVersionUID = 4L;
    final String message;

    public CustomerExistsException(String message) {
        super(message);
        this.message = message;
    }
}
