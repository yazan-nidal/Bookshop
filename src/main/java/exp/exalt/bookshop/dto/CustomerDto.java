package exp.exalt.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private long id;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private String name;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private List<BookDto> books;
}
