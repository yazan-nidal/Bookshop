package exp.exalt.bookshop.service;

import exp.exalt.bookshop.model.Book;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    private List<Book> books = new ArrayList<>(Arrays.asList(
            new Book(110000,"Code Complete",111000,112000,0),
            new Book(110001,"Clean Code",111001,112001,1),
            new Book(110001,"The Little Schemer",111002,112002,2)
    ));

    public List<Book> getAllBooks() {
        return books;
    }

    public Book getBook(long id) {
        return books.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void updateBook(Book book, long id){
        int i = 0;
        for (Book bookI:books)
        {
            if(bookI.getId() == id) {
                books.set(i,book);
                return;
            }
            i++;
        }
    }

    public void deleteBook(long id) {
        books.removeIf(b -> b.getId() == id);
    }
}
