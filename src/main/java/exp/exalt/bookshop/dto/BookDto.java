package exp.exalt.bookshop.dto;

import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private long isbn;
    private String name;
    private Author author;
    private Customer customer;
    private long id;

    public BookDto(Book book) {
        this.isbn = book.getIsbn();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.customer = book.getCustomer();
        this.id = book.getId();
    }
}
