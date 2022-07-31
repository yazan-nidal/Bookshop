package exp.exalt.bookshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void addBooks(List<Book> books) {
        this.books.addAll(books);
    }

    public boolean removeBooks(Predicate<? super Book> filter) {
        return books.removeIf(filter);
    }

    public List<Book> getBooks(Predicate<? super Book> filter) {
        List<Book> bookList = new ArrayList<>();
        books.stream().filter(filter).forEach(bookList::add);
        return bookList;
    }

    public Book getBookByIsbn(long isbn){
        return books.stream().filter(book -> book.getIsbn() == isbn).findFirst().orElse(null);
    }

    public boolean removeAllBooks() {
        return this.getBooks().removeAll(this.books);
    }
}
