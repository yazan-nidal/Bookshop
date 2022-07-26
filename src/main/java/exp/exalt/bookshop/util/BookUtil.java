package exp.exalt.bookshop.util;

import exp.exalt.bookshop.dto.BookDto;
import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.exceptions.book_exceptions.BookAuthorNotNullException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookExistsException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookNotFoundException;
import exp.exalt.bookshop.exceptions.book_exceptions.EmptyBookListException;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static exp.exalt.bookshop.constants.ConstVar.*;

@Service
public class BookUtil {
    @Autowired
    BookService bookService;
    @Autowired
    Mapper mapper;

    public List<BookDto> getBooks() {
        List<Book> books = bookService.getBooks();
        if(books.isEmpty()) {
            throw new EmptyBookListException();
        }
        return books.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    public BookDto getBookByIsbn(long isbn) {
        Book book = bookService.getBookByIsbn(isbn);
        if(book == null){
            throw new BookNotFoundException(BOOK_NOT_FOUND);
        }
        return mapper.convertToDto(book);
    }

    public List<BookDto> getBooksByName(String name) {
        List<Book> books = bookService.getBooksByName(name);
        if(books.isEmpty()) {
            throw new BookNotFoundException(BOOK_NOT_FOUND);
        }
        return books.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    public BookDto addBook(BookDto book) throws BookAuthorNotNullException {
        try {
            if(bookService.getBookByIsbn(book.getIsbn()) != null) {
                throw new BookExistsException(BOOK_CONFLICT);
            }
            bookService.addBook(mapper.convertToEntity(book));
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
