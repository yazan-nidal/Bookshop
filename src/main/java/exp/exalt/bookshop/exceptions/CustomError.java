package exp.exalt.bookshop.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public  class CustomError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String detail;

    private CustomError() {
        timestamp = LocalDateTime.now();
    }

    public CustomError(HttpStatus status, String message, String detail) {
        this();
        this.status = status;
        this.message = message;
        this.detail = detail;
    }

}