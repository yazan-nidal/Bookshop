package exp.exalt.bookshop.dto.customer_dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private List<CustomerBookDto> books;
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
