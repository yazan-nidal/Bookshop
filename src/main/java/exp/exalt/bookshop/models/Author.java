package exp.exalt.bookshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.function.Predicate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Author")
public class Author {
    @Id
    private long id;
    @NotNull
    private String name;
    @JsonBackReference
    @OneToMany
    private List<Book> books;

    public void addBook(Book book) {
        this.books.add(book);
    }

    public boolean removeBook(Predicate<? super Book> filter) {
        return books.removeIf(filter);
    }
}
