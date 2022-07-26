package exp.exalt.bookshop.services;

import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    public Book getBookByIsbn(long isbn) {
        return bookRepository.findByIsbn(isbn).orElse(null);
    }

    public List<Book> getBooksByName(String name) {
        List<Book> books = new ArrayList<>();
        bookRepository.findAllByName(name).forEach(books::add);
        return books;
    }

    public Book addBook(Book book) {
        bookRepository.save(book);
        return book;
    }

    public void deleteBook(long isbn) {
        bookRepository.deleteByIsbn(isbn);
    }
}