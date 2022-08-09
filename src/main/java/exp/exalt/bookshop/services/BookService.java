package exp.exalt.bookshop.services;

import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Iterable<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book getBookByIsbn(long isbn) {
        return bookRepository.findByIsbn(isbn).orElse(null);
    }

    @Transactional
    public Book getBookById(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Book> getBooksByName(String name) {
       return bookRepository.findAllByName(name);
    }

    @Transactional
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }
}