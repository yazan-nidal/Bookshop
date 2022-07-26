package exp.exalt.bookshop.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

interface SubError {

}

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
class ValidationError implements SubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
