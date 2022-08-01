package exp.exalt.bookshop.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import exp.exalt.bookshop.constants.RoleID;
import exp.exalt.bookshop.dto.author_dto.AuthorBookDto;
import exp.exalt.bookshop.dto.author_dto.AuthorDto;
import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.exceptions.author_exceptions.*;
import exp.exalt.bookshop.exceptions.book_exceptions.BookExistsException;
import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.models.Role;
import exp.exalt.bookshop.services.AuthorService;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static exp.exalt.bookshop.constants.ConstVar.*;

@Service
public class AuthorUtil {
    @Autowired
    AuthorService authorService;
    @Autowired
    Mapper mapper;

    public List<AuthorDto> getAuthors() throws AuthorGeneralException {
        List<Author> authors;
        List<AuthorDto> authorDtoList;
        try {
            authors = getALLAuthorsFromAuthorService();
            if (authors.isEmpty()) {
                throw new EmptyAuthorListException();
            }
            authorDtoList = mapListForm(authors,AuthorDto.class);
        } catch (IllegalStateException
                | NullPointerException exception) {
            throw new EmptyAuthorListException();
        }
        return authorDtoList;
    }

    public AuthorDto getAuthorById(long id) throws AuthorGeneralException {
        Author author;
        AuthorDto authorDto;
        try {
            author = authorService.getAuthorById(id);
            if (author == null) {
                throw new AuthorNotFoundException();
            }
            authorDto = convertNewForm(author,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException("The author id: " + ID_IS_NULL);
        } catch (NullPointerException ex) {
            throw new AuthorNotFoundException();
        }
        return authorDto;
    }

    public List<AuthorDto> getAuthorsByName(String name) throws AuthorGeneralException {
        List<Author> authors;
        List<AuthorDto> authorDtoList;
        try {
            authors = authorService.getAuthorsByName(name);
            if(authors.isEmpty()) {
                throw new AuthorNotFoundException();
            }
            authorDtoList = mapListForm(authors,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException("Authors Name: " + NAME_IS_NULL);
        } catch (IllegalStateException
                 | NullPointerException exception) {
            throw new AuthorNotFoundException();
        }
        return authorDtoList;
    }

    public AuthorDto addAuthor(AuthorDto author) throws AuthorGeneralException{
        AuthorDto authorDto;
        try {
            if(author.getId() == 0) {
                throw new AuthorNull();
            }
            if(authorService.getAuthorById(author.getId()) != null) {
                throw new AuthorExistsException(AUTHOR_CONFLICT);
            }
            author.getRoles().add(new Role(2, RoleID.AUTHOR.getRole()));
            authorDto = addAuthorWithOptionalBooks(author);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL
                    + OR + AUTHOR_NULL);
        } catch (NullPointerException
                | TransactionSystemException ex) {
            throw new AuthorNull();
        } catch (EntityExistsException ex) {
            throw new AuthorGeneralException("JPA: " + AUTHOR_CONFLICT);
        }
        return authorDto;
    }


    public AuthorDto updateAuthor(long id,AuthorDto author) throws AuthorGeneralException{
        AuthorDto authorDto;
        try {
            Author authorI = authorService.getAuthorById(id);
            if(authorI == null ) {
                throw new AuthorNull();
            }
            if(author.getId() != 0
                    && author.getId() != id) { throw new AuthorGeneralException(""); }
            author.setId(id);
            authorDto = updateAuthorWithOptionalBooks(author,authorI);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL
                    + OR + AUTHOR_NULL);
        } catch (NullPointerException
                 | TransactionSystemException ex) {
            throw new AuthorNull();
        } catch (EntityExistsException ex) {
            throw new AuthorGeneralException("JPA: " + AUTHOR_CONFLICT);
        }
        return authorDto;
    }

    public AuthorDto deleteAuthor(long id) throws AuthorGeneralException {
        AuthorDto authorDto;
        try {
            Author author = authorService.getAuthorById(id);
            if(author == null){
                throw new AuthorNotFoundException();
            }
            authorService.deleteAuthor(id);
            authorDto = convertNewForm(author,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL);
        } catch (NullPointerException ex) {
            throw new AuthorNotFoundException();
        }
        return authorDto;
    }

    public AuthorDto addAuthorBook(long id, BookDto bookDto) throws AuthorGeneralException {
        AuthorDto authorDto;
        try {
            Author author = authorService.getAuthorById(id);
            if(author == null){
                throw new AuthorNotFoundException();
            }
            authorDto = convertNewForm(addBookToAuthor(author,bookDto),AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL);
        } catch (NullPointerException ex) {
            throw new AuthorNull();
        }
        return authorDto;
    }

    public AuthorDto addAuthorBooks(long id, List<BookDto> bookDtoList) throws AuthorGeneralException {
        AuthorDto authorDto;
        try {
            Author author = authorService.getAuthorById(id);
            if(author == null){
                throw new AuthorNotFoundException();
            }
            List<Book> books = mapListForm(bookDtoList,Book.class);
            if(books  == null
                    || books.isEmpty()){
                throw new AuthorBookNull();
            }
            authorDto = convertNewForm(addAuthorBooks(author,books),AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL);
        } catch (NullPointerException
                 |  IllegalStateException ex) {
            throw new AuthorNull();
        }
        return authorDto;
    }

    public AuthorBookDto deleteAuthorBook(long id, long isbn) throws AuthorGeneralException {
        AuthorBookDto bookDto;
        try {
            Author author = authorService.getAuthorById(id);
            if(author == null){
                throw new AuthorNotFoundException();
            }
            bookDto = convertNewForm(deleteAuthorBook(author,isbn),AuthorBookDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL);
        } catch (NullPointerException ex) {
            throw new AuthorNotFoundException();
        }
        return bookDto;
    }

    public List<AuthorBookDto> deleteAuthorBooks(long id) throws AuthorGeneralException {
        Author author;
        List<AuthorBookDto> bookDtoList;
        try {
            author = authorService.getAuthorById(id);
            if(author == null){
                throw new AuthorNotFoundException();
            }
            bookDtoList = mapListForm(deleteAuthorBooks(author), AuthorBookDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL);
        }  catch (NullPointerException ex) {
            throw new AuthorNotFoundException();
        }
        return bookDtoList;
    }

    public List<AuthorBookDto> getAuthorBooks(long id) throws AuthorGeneralException {
        List<AuthorBookDto> bookDtoList;
        try {
            Author author = authorService.getAuthorById(id);
            if(author == null){
                throw new AuthorNotFoundException();
            }
            List<Book> books = author.getBooks();
            if(books.isEmpty()){
                throw new AuthorBookNotFound();
            }
            bookDtoList = mapListForm(books,AuthorBookDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL);
        } catch (NullPointerException
                |  IllegalStateException ex) {
           throw new AuthorBookNotFound();
        }
        return bookDtoList;
    }

    public AuthorBookDto getAuthorBookByIsbn(long id, long isbn) throws AuthorGeneralException {
        AuthorBookDto bookDto;
        try {
            Author author = authorService.getAuthorById(id);
            if(author == null){
                throw new AuthorNotFoundException();
            }
            Book book = author.getBookByIsbn(isbn);
            if(book == null){
                throw new AuthorBookNotFound();
            }
            bookDto = convertNewForm(book,AuthorBookDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL);
        } catch (NullPointerException
                | UnsupportedOperationException ex) {
            throw new AuthorBookNotFound();
        }
        return bookDto;
    }

    public List<AuthorBookDto> getAuthorBooksByName(long id, String name) throws AuthorGeneralException {
        List<AuthorBookDto> bookDtoList;
        try {
            Author author = authorService.getAuthorById(id);
            if(author == null){
                throw new AuthorNotFoundException();
            }
            List<Book> books =  author.getBooks(book -> book.getName().equals(name));
            if(books.isEmpty()){
                throw new AuthorBookNotFound();
            }
            bookDtoList = mapListForm(books,AuthorBookDto.class);
        } catch (IllegalArgumentException ex) {
            throw new AuthorGeneralException(AUTHOR_ID_IS_NULL);
        } catch (NullPointerException
                 |  IllegalStateException ex) {
            throw new AuthorBookNotFound();
        } catch (UnsupportedOperationException
                | ClassCastException ex) {
            throw new AuthorGeneralException(BOOK_LIST_PROBLEM  + CONTACT_ADMIN);
        }
        return bookDtoList;
    }

    public  AuthorDto updateAuthor(long id,JsonPatch patch) {
        try {
            Author author = authorService.getAuthorById(id);
            if(author == null) {
                throw  new AuthorBookNotFound();
            }
            Author authorPatched = applyPatchToAuthor(patch, author);
            return convertNewForm( authorService.addAuthorOrUpdate(authorPatched),AuthorDto.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new AuthorGeneralException("Patch Exception");
        } catch (NullPointerException |
                IllegalArgumentException e) {
            throw new AuthorNotFoundException();
        }
    }

    // helper method
    private Author applyPatchToAuthor(
            JsonPatch patch, Author targetAuthor) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(mapper.convertForm(targetAuthor, JsonNode.class));
        return mapper.convertForm(patched, Author.class);
    }

    private List<Author> getALLAuthorsFromAuthorService(){
        Iterable<Author> authorIterable;
        List<Author> authors;
        try {
             authorIterable = authorService.getAuthors();
             authors = new ArrayList<>();
             authorIterable.forEach(authors::add);
        } catch (UnsupportedOperationException
                | ClassCastException
                | IllegalArgumentException ex) {
                    throw new AuthorGeneralException("it' have problem in get Authors (prepare list operation):"
                            + FOR_EACH_ACTION + CONTACT_ADMIN );
        } catch (NullPointerException ex) {
            throw new EmptyAuthorListException();
        }
        return authors;
    }

    private Author addBookToAuthor(Author author, BookDto bookDto){
      try {
          if(bookDto == null
          || bookDto.getIsbn() == 0){
              throw new AuthorBookNull();
          }
          if((author.getBookByIsbn(bookDto.getIsbn()) != null)) {
              throw new BookExistsException(BOOK_EXISTS);
          }
          Book book = convertNewForm(bookDto,Book.class);
          book.setAuthor(author);
          author.addBook(book);
          author =  authorService.addAuthorOrUpdate(author);
      } catch (NullPointerException
              | TransactionSystemException ex) {
          throw new AuthorBookNull();
      } catch (ClassCastException
               | UnsupportedOperationException
               | IllegalArgumentException ex){
          throw new AuthorGeneralException(BOOK_LIST_PROBLEM + CONTACT_ADMIN);
      } catch (DataIntegrityViolationException ex) {
          throw new BookExistsException(BOOK_EXISTS);
      }
        return author;
    }

    private Author addAuthorBooks(Author author, List<Book> books){
       try {
           if(author == null
                   || books == null
                   || books.isEmpty()) {
               return null;
           }
           for(Book book: books) {
               book.setAuthor(author);
               if((book.getIsbn() == 0)
                       || (author.getBookByIsbn(book.getIsbn()) != null)) {
                   throw new BookExistsException(BOOK_EXISTS);
               }
           }
           author.addBooks(books);
           authorService.addAuthorOrUpdate(author);
           author = authorService.getAuthorById(author.getId());
       } catch (NullPointerException
                | TransactionSystemException
                | IllegalArgumentException ex) {
           // AUTHOR_NOT_FOUND OR AUTHOR_BOOK_NULL
           throw new AuthorNull();
       } catch (ClassCastException
                | UnsupportedOperationException ex){
           throw new AuthorGeneralException(BOOK_LIST_PROBLEM);
       } catch (DataIntegrityViolationException ex) {
           throw new BookExistsException(BOOK_EXISTS);
       }
       return author;
    }

    private AuthorDto addAuthorWithOptionalBooks(AuthorDto author)
    {
        AuthorDto authorDto;
        try {
            Author authorI= convertNewForm(author, Author.class);
            List<Book> books = authorI.getBooks();
            authorI.setBooks(new ArrayList<>());
            authorDto = convertNewForm(authorService.addAuthorOrUpdate(authorI),AuthorDto.class);
            if(books != null
                    && !books.isEmpty()) {
                authorDto = convertNewForm(addAuthorBooks(authorI,books),AuthorDto.class);
            }
        } catch (IllegalArgumentException ex) {
            throw new AuthorNull();
        }
        return authorDto;
    }

    private AuthorDto updateAuthorWithOptionalBooks(AuthorDto authorDto,Author author)
    {
        try {
            Author authorI= convertNewForm(authorDto, Author.class);
            if(authorDto.getBooks() != null ) {
                if(!authorDto.getBooks().isEmpty()) {
                    if(author.getBooks() != null
                    && !author.getBooks().isEmpty()) {
                        this.deleteAuthorBooks(author.getId());
                    }
                }
            } else {
                authorI.setBooks(author.getBooks());
                authorService.addAuthorOrUpdate(authorI);
                return convertNewForm(authorI,AuthorDto.class);
            }
            List<Book> books = new ArrayList<>(authorI.getBooks());
            authorI.setBooks(new ArrayList<>());
           authorDto = convertNewForm(this.addAuthorBooks(authorI, books),AuthorDto.class);
        } catch (IllegalArgumentException
                | NullPointerException ex) {
            throw new AuthorNull();
        }
        return authorDto;
    }

    private BookDto deleteAuthorBook(Author author, long isbn) {
        BookDto bookDto;
        try {
            Book book = author.getBookByIsbn(isbn);
            if(book == null){
                throw new AuthorBookNotFound();
            }
            // Removes all the elements of this collection that satisfy the given predicate.
            // but isbn is unique so it's remove one.
            author.removeBooks(b -> b.getIsbn() == isbn);
            authorService.addAuthorOrUpdate(author);
            bookDto = convertNewForm(book,BookDto.class);
        } catch (NullPointerException
                 | IllegalArgumentException ex) {
            throw new AuthorBookNotFound();
        } catch (UnsupportedOperationException ex) {
            throw new AuthorGeneralException(BOOKS_REMOVE_PROBLEM + " plz contact admin.");
        }
        return bookDto;
    }

    private List<BookDto> deleteAuthorBooks(Author author) {
        List<BookDto> bookDtoList;
        try {
            List<Book> books = new ArrayList<>(author.getBooks());
            if(books.isEmpty())
            {
                throw new AuthorBookNotFound();
            }
            author.removeAllBooks();
            authorService.addAuthorOrUpdate(author);
            bookDtoList = mapListForm(books,BookDto.class);
        } catch (NullPointerException
                 | IllegalArgumentException
                 | IllegalStateException ex) {
            throw new AuthorBookNotFound();
        } catch (UnsupportedOperationException
                | ClassCastException ex) {
            throw new AuthorGeneralException(BOOKS_REMOVE_PROBLEM + " plz contact admin.");
        }
        return bookDtoList;
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
