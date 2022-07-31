package exp.exalt.bookshop.util;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.exceptions.author_exceptions.AuthorGeneralException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookAuthorNotNullException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookExistsException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookNotFoundException;
import exp.exalt.bookshop.exceptions.book_exceptions.EmptyBookListException;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.services.BookService;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        Iterable<Book> booksI = bookService.getBooks();
        List<Book> books = new ArrayList<>();
       booksI.forEach(books::add);
        return mapListForm(books,BookDto.class);
    }

    public BookDto getBookByIsbn(long isbn) {
        return convertNewForm(bookService.getBookByIsbn(isbn), BookDto.class);
    }

    public List<BookDto> getBooksByName(String name) {
        return mapListForm(bookService.getBooksByName(name), BookDto.class);
    }

    // map methods
    private <P,T> List<T> mapListForm(List<P> p, Class<T> dest) {
        List<T>  t;
        try {
            t = p.stream()
                    .map(pi-> mapper.convertForm(pi,dest))
                    .collect(Collectors.toList());
        } catch (ConfigurationException
                 | IllegalArgumentException
                 | MappingException ex) {
            throw new AuthorGeneralException(MODEL_MAPPER_ILLEGAL_EXCEPTION);
        }
        return t;
    }

    private <P,T> T convertNewForm(P p,Class<T> dest) {
        T t;
        try {
            t = mapper.convertForm(p,dest);
        } catch (ConfigurationException
                 | IllegalArgumentException
                 | MappingException ex) {
            throw new AuthorGeneralException(MODEL_MAPPER_ILLEGAL_EXCEPTION);
        }
        return t;
    }

}
