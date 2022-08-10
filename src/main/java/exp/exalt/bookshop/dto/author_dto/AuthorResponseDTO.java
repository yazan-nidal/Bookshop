package exp.exalt.bookshop.dto.author_dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import exp.exalt.bookshop.dto.book_dto.BookDtoO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponseDTO implements Serializable {
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private long id = Integer.MIN_VALUE;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private String name;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    @JsonManagedReference("author-book")
    private List<BookDtoO> books;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Column(nullable = true)
    String username;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Column(nullable = true)
    private int role = Integer.MIN_VALUE;
}