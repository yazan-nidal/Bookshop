package exp.exalt.bookshop.util;

import exp.exalt.bookshop.exceptions.book_exceptions.BookAuthorNotNullException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookExistsException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookNotFoundException;
import exp.exalt.bookshop.exceptions.book_exceptions.EmptyBookListException;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static exp.exalt.bookshop.constants.ConstVar.*;

@Service
public class BookUtil {
    @Autowired
    BookService bookService;

    public List<Book> getBooks() {
        List<Book> books = bookService.getBooks();
        if(books.isEmpty()) {
            throw new EmptyBookListException();
        }
        return books;
    }

    public Book getBookByIsbn(long isbn) {
        Book book = bookService.getBookByIsbn(isbn);
        if(book == null){
            throw new BookNotFoundException(BOOK_NOT_FOUND);
        }
        return book;
    }

    public List<Book> getBooksByName(String name) {
        List<Book> books = bookService.getBooksByName(name);
        if(books.isEmpty()) {
            throw new BookNotFoundException(BOOK_NOT_FOUND);
        }
        return books;
    }

    public Book addBook(Book book) throws BookAuthorNotNullException {
        try {
            if(bookService.getBookByIsbn(book.getIsbn()) != null) {
                throw new BookExistsException(BOOK_CONFLICT);
            }
            bookService.addBook(book);
        } catch (Exception ex) {
              throw new BookAuthorNotNullException(ex.getMessage());
        }
        return book;
    }

    public void deleteBook(long isbn) {
        Book book = bookService.getBookByIsbn(isbn);
        if(book == null){
            throw new BookNotFoundException(BOOK_NOT_FOUND);
        }
        bookService.deleteBook(isbn);
    }
}
