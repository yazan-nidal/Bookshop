package exp.exalt.bookshop.dto;

import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.models.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private long id;
    private String name;
    private List<Book> books;

    public AuthorDto(Author author)
    {
        this.id = author.getId();
        this.name = author.getName();
        this.setBooks(author.getBooks());
    }
}
