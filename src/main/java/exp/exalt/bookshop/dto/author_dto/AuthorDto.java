package exp.exalt.bookshop.dto.author_dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import exp.exalt.bookshop.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto implements Serializable {
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private long id;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Nullable
    private String name;
    @Nullable
    private List<AuthorBookDto> books;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Column(nullable = true)
    String username;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Column(nullable = true)
    String password;
    @JsonInclude(JsonInclude.Include.CUSTOM)
    @Column(nullable = true)
    private Set<Role> roles = new HashSet<>();
}
