package exp.exalt.bookshop.exceptions.book_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class BookAuthorNotNullException extends RuntimeException{
        private static final long serialVersionUID = 10L;
        final String message;

        public BookAuthorNotNullException(String message) {
            super(message);
            this.message = message;
        }
}
