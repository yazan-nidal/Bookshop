package exp.exalt.bookshop.exceptions.customer_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 5L;
    final String message;

    public CustomerNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
