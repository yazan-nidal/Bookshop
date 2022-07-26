package exp.exalt.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto implements Serializable {
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private long isbn;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private String name;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private AuthorDto author;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private CustomerDto customer;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private long id;
}
