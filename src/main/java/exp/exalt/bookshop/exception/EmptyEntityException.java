package exp.exalt.bookshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class EmptyEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmptyEntityException(String message) {
        super(message);
    }

}