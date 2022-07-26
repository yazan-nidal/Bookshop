package exp.exalt.bookshop.exceptions.customer_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class EmptyCustomerListException extends RuntimeException {
    private static final long serialVersionUID = 6L;

    public EmptyCustomerListException() {
        super();
    }
}
