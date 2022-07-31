package exp.exalt.bookshop.services;

import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Iterable<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book getBookByIsbn(long isbn) {
        return bookRepository.findByIsbn(isbn).orElse(null);
    }

    public List<Book> getBooksByName(String name) {
       return bookRepository.findAllByName(name);
    }
}