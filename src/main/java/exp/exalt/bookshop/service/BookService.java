package exp.exalt.bookshop.service;

import exp.exalt.bookshop.model.Book;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookService {

    private List<Book> books = Arrays.asList(
            new Book(110000,"Code Complete",111000,112000,0),
            new Book(110001,"Clean Code",111001,112001,1),
            new Book(110001,"The Little Schemer",111002,112002,2)
    );

    public List<Book> getAllBooks() {
        return books;
    }
}
