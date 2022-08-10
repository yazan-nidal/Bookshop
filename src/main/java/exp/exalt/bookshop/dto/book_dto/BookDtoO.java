package exp.exalt.bookshop.dto.book_dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import exp.exalt.bookshop.dto.author_dto.AuthorResponseDTO;
import exp.exalt.bookshop.dto.customer_dto.CustomerDtoO;
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
public class BookDtoO implements Serializable {
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private long isbn = 0;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private String name;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    @JsonBackReference("author-book")
    private AuthorResponseDTO author;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    @JsonBackReference("customer-book")
    private CustomerDtoO customer;
    @Nullable
    @JsonInclude(JsonInclude.Include.CUSTOM)
    private long id = 0;
}
