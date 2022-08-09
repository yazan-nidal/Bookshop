package exp.exalt.bookshop.dto.book_dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import exp.exalt.bookshop.dto.author_dto.AuthorDto;
import exp.exalt.bookshop.dto.customer_dto.CustomerDto;
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
    private long isbn = Integer.MIN_VALUE;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private String name;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    @JsonBackReference("author-book")
    private AuthorDto author;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    @JsonBackReference("customer-book")
    private CustomerDto customer;
    @Nullable
    @JsonInclude(JsonInclude.Include.CUSTOM)
    private long id = Integer.MIN_VALUE;
}
