package exp.exalt.bookshop.util;

import exp.exalt.bookshop.services.BookService;
import org.apache.log4j.Logger;
import exp.exalt.bookshop.dto.author_dto.AuthorDto;
import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.exceptions.author_exceptions.*;
import exp.exalt.bookshop.exceptions.book_exceptions.BookExistsException;
import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.models.Customer;
import exp.exalt.bookshop.models.RoleID;
import exp.exalt.bookshop.services.AuthorService;
import exp.exalt.bookshop.services.CustomerService;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import static exp.exalt.bookshop.constants.ConstVar.*;

@Service
public class AuthorUtil {
    @Autowired
    AuthorService authorService;
    @Autowired
    Mapper mapper;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    BookShopUserUtil bookShopUserUtil;
    @Autowired
    CustomerService customerService;
    @Autowired
    BookService bookService;

    static Logger log = Logger.getLogger(AuthorUtil.class.getName());

    public List<AuthorDto> getAuthors(String auth) throws AuthorGeneralException {
        List<Author> authors;
        List<AuthorDto> authorDtoList;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            authors = getALLAuthorsFromAuthorService(username_log);
            if (authors.isEmpty()) {
                String message =AUTHOR_NOT_EXISTS;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new EmptyAuthorListException();
            }
            authorDtoList = mapListForm(authors,AuthorDto.class);
        } catch (IllegalStateException
                 | NullPointerException exception) {
            String message =AUTHOR_NOT_EXISTS +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }
        return authorDtoList;
    }

    private List<Author> getALLAuthorsFromAuthorService(String username_log){
        Iterable<Author> authorIterable;
        List<Author> authors;
        try {
             authorIterable = authorService.getAuthors();
             authors = new ArrayList<>();
             authorIterable.forEach(authors::add);
        } catch (UnsupportedOperationException
                | ClassCastException
                | IllegalArgumentException ex) {
            String message ="it' have problem in get Authors (prepare list operation):"
                    + FOR_EACH_ACTION + CONTACT_ADMIN;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
                    throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }
        return authors;
    }

    public AuthorDto getAuthorByUsername(String author_username,String auth) throws AuthorGeneralException {
        Author author;
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException();
            }
            authorDto = convertNewForm(author,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return authorDto;
    }

    public AuthorDto getAuthorById(long id,String auth) throws AuthorGeneralException {
        Author author;
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException();
            }
            authorDto = convertNewForm(author,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return authorDto;
    }

    public List<AuthorDto> getAuthorsByName(String name,String auth) throws AuthorGeneralException {
        List<Author> authors;
        List<AuthorDto> authorDtoList;
            String username_log = "";
            try {
                String username = jwtUtil.getUserName(auth);
                username_log = username;
                if(username == null
                        || username.isEmpty()) {
                    String message =BAD_CREDENTIAL;
                    log.warn(String.format(
                            LOG_INSERT
                            ,auth
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
                }
                if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                    String message =USER_DEACTIVATED;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
                }
            authors = authorService.getAuthorsByName(name);
            if(authors.isEmpty()) {
                String message =AUTHOR_NOT_EXISTS + " \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.NO_CONTENT);
            }
            authorDtoList = mapListForm(authors,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
                String message ="Authors Name: " + NAME_IS_NULL+" \\ "+SERVER_ERROR;
                log.error(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"ERROR"
                        ,message));
                throw new AuthorNullException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalStateException
                 | NullPointerException exception) {
                String message =SERVER_ERROR;
                log.error(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"ERROR"
                        ,message));
            throw new AuthorNotFoundException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return authorDtoList;
    }

    public AuthorDto addAuthor(AuthorDto author, String auth) throws AuthorGeneralException{
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(author == null
                    || author.getUsername() == null
                    || author.getUsername().isEmpty()) {
                String message =AUTHOR_NULL_BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNullException(message, HttpStatus.BAD_REQUEST);
            }
            if(author.getId() != Integer.MIN_VALUE){
                String message =ASSIGN_ID_ERROR;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(author.getUsername() == null
                    || author.getUsername().isEmpty()
                    || author.getUsername().length() < USERNAME_LENGTH) {
                String message =USERNAME_LENGTH_Problem;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message, HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserByUsername(author.getUsername()) != null) {
                String message =USERNAME_CONFLICT;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorExistsException(message);
            }
            if(author.getPassword() == null
                    || author.getPassword().isEmpty()
                    || author.getPassword().length() < PASSWORD_LENGTH) {
                    String message = PASSWORD_LENGTH_Problem + " \\ " + BAD_REQUEST;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(author.getRole() == Integer.MIN_VALUE){
                author.setRole(RoleID.AUTHOR.getRole());
            }
            if(author.getRole() != RoleID.AUTHOR.getRole()
                    && author.getRole() != RoleID.ADMIN.getRole()) {
                String message =AUTHOR_ROLE_BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNullException(message, HttpStatus.BAD_REQUEST);
            }
            authorDto = addAuthorWithOptionalBooks(author,username_log);
        } catch (IllegalArgumentException
                 | NullPointerException
                 | TransactionSystemException ex) {
            String message =AUTHOR_NULL+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNullException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityExistsException
                 | DataIntegrityViolationException ex) {
            String message = "JPA: " + USER_CONFLICT+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        authorDto.setPassword(null);
        return authorDto;
    }

    private AuthorDto addAuthorWithOptionalBooks(AuthorDto author,String username_log) {
        AuthorDto authorDto;
        try {
            Author authorI = convertNewForm(author, Author.class);
            List<Book> books = authorI.getBooks();
            authorI.setBooks(new ArrayList<>());
            authorI = authorService.addAuthorOrUpdate(authorI);
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,"A new author (" + author.getUsername() +") has been added by this admin (" + username_log+")"));
            authorDto = convertNewForm(authorI,AuthorDto.class);
            if(books != null
                    && !books.isEmpty()) {
                authorDto = convertNewForm(addAuthorBooks(authorI,books,username_log),AuthorDto.class);
            }
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_NULL+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNullException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return authorDto;
    }

    private Author addAuthorBooks(Author author, List<Book> books,String username_log){
        try {
            if(author == null
                    || books == null
                    || books.isEmpty()) {
                return null;
            }
            boolean exists = false;
            boolean force_rent = false;
            String books_exists = "";
            String forced_rent_books = "";

            List<Book> newBooks = new ArrayList<>();
            for(Book book: books) {
                book.setAuthor(author);
                // handel add book with customer
                if(book.getCustomer() != null) {
                    force_rent = true;
                    forced_rent_books += book.getName() + " : " + book.getIsbn() +" to customer ( "+book.getCustomer().getUsername()+" ) /";
                    book.setCustomer(null);
                }
                if((book.getIsbn() == 0)
                        || (bookService.getBookByIsbn(book.getIsbn()) != null)) {
                    exists = true;
                    books_exists += book.getName()+" : "+book.getIsbn()+" / ";
                    //books.remove(book);
                } else {
                    newBooks.add(book);
                }
            }
            author.addBooks(newBooks);
            String author_books = "";
            for (Book book : newBooks) {
                author_books += book.getName()+" : "+book.getIsbn()+" / ";
            }
            authorService.addAuthorOrUpdate(author);
            String message = author.getUsername().equals(username_log)?
                    "admin (" + username_log +") added  books[ "+author_books+"] of  author  when  add  the author (" + author.getUsername() +")":
                    "author (" + author.getUsername()+") add  new  books[ "+author_books+"]";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
            author = authorService.getAuthorByUsername(author.getUsername());
            if(exists || force_rent){
                message = "";
                if(force_rent) {
                    message = FORCE_RENT_BOOKS+" [ "+ forced_rent_books+" ]  // ";
                }
                if(exists) {
                   message+= BOOK_EXISTS_BAD_REQUEST+" : [ "+books_exists+" ] but the other books were added";
                }
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new BookExistsException(message);
            }
        } catch (NullPointerException
                 | TransactionSystemException
                 | IllegalArgumentException ex) {
            // AUTHOR_NOT_FOUND OR AUTHOR_BOOK_NULL
            String message = AUTHOR_NULL+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNullException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ClassCastException
                 | UnsupportedOperationException ex){
            String message = BOOK_LIST_PROBLEM+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (DataIntegrityViolationException ex) {
            String message = BOOK_EXISTS+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new BookExistsException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return author;
    }

    public AuthorDto deleteAuthor(long id, String auth) throws AuthorGeneralException {
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
           if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || bookShopUserUtil.getUserByUsername(username).getId() != id) ) {
               String message =UNAUTHORIZED_OPERATION;
               log.warn(String.format(
                       LOG_INSERT
                       ,username_log
                       ,DATE_FORMAT.format(new Date())
                       ,this.getClass().getName()
                       ,"WARN"
                       ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException();
            }
            this.deleteAuthorBooks(author,username);
            authorService.deleteAuthor(id);
            String message ="user ("+username_log+") deleted this author ("+author.getUsername()+") and wrote it";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
            //map author with book  have  customer , to  prevent recursive
            authorDto = convertNewForm(author,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        authorDto.setPassword(null);
        return authorDto;
    }

    public AuthorDto deleteAuthor(String author_username, String auth) throws AuthorGeneralException {
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || !bookShopUserUtil.getUserByUsername(username).getUsername().equals(author_username)) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException();
            }
            this.deleteAuthorBooks(author,username);
            authorService.deleteAuthor(author_username);
            String message ="user ("+username_log+") deleted this author ("+author.getUsername()+") and wrote it";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
            //map author with book  have  customer , to  prevent recursive
            authorDto = convertNewForm(author,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        authorDto.setPassword(null);
        return authorDto;
    }

    public AuthorDto addAuthorBook(long id, String auth, BookDto bookDto) throws AuthorGeneralException {
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || bookShopUserUtil.getUserByUsername(username).getId() != id) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
           Author authorI = addBookToAuthor(author,bookDto,username_log);
            authorDto = convertNewForm(authorI,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        authorDto.setPassword(null);
        return authorDto;
    }

    public AuthorDto addAuthorBook(String author_username, String auth, BookDto bookDto) throws AuthorGeneralException {
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || !bookShopUserUtil.getUserByUsername(username).getUsername().equals(author_username)) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            Author authorI = addBookToAuthor(author,bookDto,username_log);
            authorDto = convertNewForm(authorI,AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        authorDto.setPassword(null);
        return authorDto;
    }

    private Author addBookToAuthor(Author author, BookDto bookDto, String username_log){
      try {
          if(bookDto == null
          || bookDto.getIsbn() == 0){
              String message =AUTHOR_BOOK_NULL +" \\ "+BAD_REQUEST;
              log.warn(String.format(
                      LOG_INSERT
                      ,username_log
                      ,DATE_FORMAT.format(new Date())
                      ,this.getClass().getName()
                      ,"WARN"
                      ,message));
              throw new AuthorBookNull(message,HttpStatus.BAD_REQUEST);
          }
          if((bookService.getBookByIsbn(bookDto.getIsbn()) != null)) {
              String message = BOOK_EXISTS_BAD_REQUEST +" \\ "+BAD_REQUEST;
              log.warn(String.format(
                      LOG_INSERT
                      ,username_log
                      ,DATE_FORMAT.format(new Date())
                      ,this.getClass().getName()
                      ,"WARN"
                      ,message));
              throw new BookExistsException(message,HttpStatus.CONFLICT);
          }
          if(bookDto.getCustomer() != null) {
              String message = FOR_EACH_ACTION +" \\ "+BAD_REQUEST;
              log.warn(String.format(
                      LOG_INSERT
                      ,username_log
                      ,DATE_FORMAT.format(new Date())
                      ,this.getClass().getName()
                      ,"WARN"
                      ,message));
              throw new AuthorGeneralException(message,HttpStatus.CONFLICT);
          }
          Book book = convertNewForm(bookDto,Book.class);
          book.setAuthor(author);
          book.setId(0);
          author.addBook(book);
          author =  authorService.addAuthorOrUpdate(author);
          String message ="user ("+username_log+") add book("+book.getName()+" : "+book.getIsbn()+") for author ("+author.getUsername()+")";
          log.info(String.format(
                  LOG_INSERT
                  ,username_log
                  ,DATE_FORMAT.format(new Date())
                  ,this.getClass().getName()
                  ,"INFO"
                  ,message));
      } catch (NullPointerException
              | TransactionSystemException ex) {
          String message = AUTHOR_BOOK_NULL +" \\ "+SERVER_ERROR;
          log.error(String.format(
                  LOG_INSERT
                  ,username_log
                  ,DATE_FORMAT.format(new Date())
                  ,this.getClass().getName()
                  ,"ERROR"
                  ,message));
          throw new AuthorBookNull(message,HttpStatus.INTERNAL_SERVER_ERROR);
      } catch (ClassCastException
               | UnsupportedOperationException
               | IllegalArgumentException ex){
          String message = BOOK_LIST_PROBLEM +" \\ "+SERVER_ERROR;
          log.error(String.format(
                  LOG_INSERT
                  ,username_log
                  ,DATE_FORMAT.format(new Date())
                  ,this.getClass().getName()
                  ,"ERROR"
                  ,message));
          throw new AuthorGeneralException(message);
      } catch (DataIntegrityViolationException ex) {
          String message = BOOK_EXISTS_BAD_REQUEST +" \\ "+SERVER_ERROR;
          log.error(String.format(
                  LOG_INSERT
                  ,username_log
                  ,DATE_FORMAT.format(new Date())
                  ,this.getClass().getName()
                  ,"ERROR"
                  ,message));
          throw new BookExistsException(message,HttpStatus.INTERNAL_SERVER_ERROR);
      }
        return author;
    }

    public AuthorDto addAuthorBooks(long id, List<BookDto> bookDtoList,String auth) throws AuthorGeneralException {
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || bookShopUserUtil.getUserByUsername(username).getId() != id) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            List<Book> books = mapListForm(bookDtoList,Book.class);
            if(books  == null
                    || books.isEmpty()){
                String message = AUTHOR_BOOK_NULL +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNull(message,HttpStatus.BAD_REQUEST);
            }
            authorDto = convertNewForm(addAuthorBooks(author,books,username_log),AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException
                 |  IllegalStateException ex) {
            String message = AUTHOR_NULL_BAD_REQUEST +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNullException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        authorDto.setPassword(null);
        return authorDto;
    }

    public AuthorDto addAuthorBooks(String author_username, List<BookDto> bookDtoList,String auth) throws AuthorGeneralException {
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || !bookShopUserUtil.getUserByUsername(username).getUsername().equals(author_username)) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            List<Book> books = mapListForm(bookDtoList,Book.class);
            if(books  == null
                    || books.isEmpty()){
                String message = AUTHOR_BOOK_NULL +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNull(message,HttpStatus.BAD_REQUEST);
            }
            authorDto = convertNewForm(addAuthorBooks(author,books,username_log),AuthorDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException
                 |  IllegalStateException ex) {
            String message = AUTHOR_NULL_BAD_REQUEST +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNullException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        authorDto.setPassword(null);
        return authorDto;
    }

    public BookDto deleteAuthorBookByIsbn(long id, long isbn, String auth) throws AuthorGeneralException {
        BookDto bookDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || bookShopUserUtil.getUserByUsername(username).getId() != id) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookByIsbn(isbn);
            if(book == null){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.BAD_REQUEST);
            }
            bookDto = convertNewForm(deleteAuthorBook(author,(b -> b.getId() != id),book,username_log),BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDto;
    }

    public BookDto deleteAuthorBookByIsbn(String author_username, long isbn, String auth) throws AuthorGeneralException {
        BookDto bookDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || !bookShopUserUtil.getUserByUsername(username).getUsername().equals(author_username)) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookByIsbn(isbn);
            if(book == null){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.BAD_REQUEST);
            }
            bookDto = convertNewForm(deleteAuthorBook(author,(b -> b.getIsbn() == isbn),book,username_log),BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDto;
    }

    public BookDto deleteAuthorBookById(long id, long book_id, String auth) throws AuthorGeneralException {
        BookDto bookDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || bookShopUserUtil.getUserByUsername(username).getId() != id) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookById(book_id);
            if(book == null){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.BAD_REQUEST);
            }
            bookDto = convertNewForm(deleteAuthorBook(author,(b -> b.getId() == book_id),book,username_log),BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDto;
    }

    public BookDto deleteAuthorBookById(String author_username, long book_id, String auth) throws AuthorGeneralException {
        BookDto bookDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || !bookShopUserUtil.getUserByUsername(username).getUsername().equals(author_username)) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookById(book_id);
            if(book == null){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.BAD_REQUEST);
            }
            bookDto = convertNewForm(deleteAuthorBook(author,(b -> b.getId() == book_id),book,username_log),BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDto;
    }

    private BookDto deleteAuthorBook(Author author, Predicate<? super Book> filter,Book book, String username_log) {
        BookDto bookDto;
        try {
            // Removes all the elements of this collection that satisfy the given predicate.
            // but isbn is unique so it's remove one.
            Customer customer = author.getBookById(book.getId()).getCustomer();
            if(customer != null) {
                customer.removeBooks(filter);
                customerService.addCustomerOrUpdate(customer);
            }
            author.getBookById(book.getId()).setCustomer(null);
            author.getBookById(book.getId()).setAuthor(null);
            author.removeBooks(filter);
            authorService.addAuthorOrUpdate(author);
            String message ="user ("+username_log+") delete book("+book.getName()+" : "+book.getIsbn()+") from author ("+author.getUsername()+")";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
            bookDto = convertNewForm(book,BookDto.class);
        } catch (NullPointerException
                 | IllegalArgumentException ex) {
            String message =(AUTHOR_BOOK_NULL +" \\ "+SERVER_ERROR);
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorBookNotFound(message,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedOperationException ex) {
            String message =(BOOKS_REMOVE_PROBLEM +" \\ "+SERVER_ERROR);
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }
        return bookDto;
    }

    public List<BookDto> deleteAuthorBooks(long id,String auth) throws AuthorGeneralException {
        Author author;
        List<BookDto> bookDtoList;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || bookShopUserUtil.getUserByUsername(username).getId() != id) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
           author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            bookDtoList = mapListForm(deleteAuthorBooks(author,username), BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }  catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDtoList;
    }

    public List<BookDto> deleteAuthorBooks(String author_username, String auth) throws AuthorGeneralException {
        Author author;
        List<BookDto> bookDtoList;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || !bookShopUserUtil.getUserByUsername(username).getUsername().equals(author_username)) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            bookDtoList = mapListForm(deleteAuthorBooks(author,username), BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }  catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDtoList;
    }

    private List<BookDto> deleteAuthorBooks(Author author,String username) {
        List<BookDto> bookDtoList;
        try {
            List<Book> books = new ArrayList<>(author.getBooks());
            if(books.isEmpty())
            {
                String message = AUTHOR_EMPTY_BOOK_LIST+" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.INTERNAL_SERVER_ERROR);
            }
            for (Book book : books) {
                if(book.getCustomer() != null) {
                    Customer customer = book.getCustomer();
                    book.setCustomer(null);
                    book.setAuthor(null);
                    customer.removeBooks(b -> b.getId() == book.getId());
                }
            }
            author.removeAllBooks();
            authorService.addAuthorOrUpdate(author);
            String message = "this user ("+username+") delete all books of this author ("+author.getUsername()+")";
            log.info(String.format(
                    LOG_INSERT
                    ,username
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
            bookDtoList = mapListForm(books,BookDto.class);
        } catch (NullPointerException
                 | IllegalArgumentException
                 | IllegalStateException ex) {
            String message = AUTHOR_NULL_BAD_REQUEST +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorBookNotFound(message,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedOperationException
                | ClassCastException ex) {
            String message = BOOKS_REMOVE_PROBLEM + " \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }
        return bookDtoList;
    }

    public AuthorDto updateAuthor(long id,AuthorDto authorI, String auth) throws AuthorGeneralException{
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || bookShopUserUtil.getUserByUsername(username).getId() != id) ) {
                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            if(authorI.getId() != Integer.MIN_VALUE
                    && authorI.getId() != 0
                    && authorI.getId() != id) {
                String message = USER_ID_CHANGE_ERROR;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            authorI.setId(author.getId());
            if(authorI.getUsername() == null
                    || authorI.getUsername().isEmpty()
                    || authorI.getUsername().equals(author.getUsername())) {
                authorI.setUsername(author.getUsername());
            }
            else {
                if(bookShopUserUtil.getUserByUsername(authorI.getUsername()) != null) {
                    String message = USERNAME_CONFLICT +" \\ "+ BAD_REQUEST;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorExistsException(message,HttpStatus.BAD_REQUEST);
                }
            }
            if(authorI.getPassword() == null
                    || authorI.getPassword().isEmpty()
                    || authorI.getPassword().equals(author.getPassword())){
                authorI.setPassword(author.getPassword());
            } else {
                if(authorI.getPassword().length() < PASSWORD_LENGTH) {
                    String message = PASSWORD_LENGTH_Problem + " \\ " + BAD_REQUEST;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
                }
            }
            if(authorI.getRole() == Integer.MIN_VALUE
                    || authorI.getRole() == author.getRole()){
                authorI.setRole(author.getRole());
            } else {
                if (authorI.getRole() == RoleID.CUSTOMER.getRole()) {
                    String message = USER_CHANGE_ROLE_PROBLEM;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
                }
                if(authorI.getRole() == RoleID.ADMIN.getRole()
                        && (bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole())) {
                    String message =UNAUTHORIZED_OPERATION;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
                }
            }
            authorDto = updateAuthorWithOptionalBooks(authorI,author,username_log,auth);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException
                | TransactionSystemException ex) {
            String message = AUTHOR_NULL_BAD_REQUEST +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorBookNotFound(message,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityExistsException
                | DataIntegrityViolationException ex) {
            String message = "JPA: " + USER_CONFLICT+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        authorDto.setPassword(null);
        return authorDto;
    }

    public AuthorDto updateAuthor(String author_username,AuthorDto authorI, String auth) throws AuthorGeneralException{
        AuthorDto authorDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || !bookShopUserUtil.getUserByUsername(username).getUsername().equals(author_username))) {

                String message =UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            if(authorI.getId() != Integer.MIN_VALUE
                    && authorI.getId() != 0
                    && authorI.getId() != author.getId()) {
                String message = USER_ID_CHANGE_ERROR;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            authorI.setId(author.getId());
            if(authorI.getUsername() == null
                    || authorI.getUsername().isEmpty()
                    || authorI.getUsername().equals(author.getUsername())) {
                authorI.setUsername(author.getUsername());
            }
            else {
                if(bookShopUserUtil.getUserByUsername(authorI.getUsername()) != null) {
                    String message = USERNAME_CONFLICT +" \\ "+ BAD_REQUEST;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorExistsException(message,HttpStatus.BAD_REQUEST);
                }
            }
            if(authorI.getPassword() == null
                    || authorI.getPassword().isEmpty()
                    || authorI.getPassword().equals(author.getPassword())){
                authorI.setPassword(author.getPassword());
            } else {
                if(authorI.getPassword().length() < PASSWORD_LENGTH) {
                    String message = PASSWORD_LENGTH_Problem + " \\ " + BAD_REQUEST;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
                }
            }
            if(authorI.getRole() == Integer.MIN_VALUE
                    || authorI.getRole() == author.getRole()){
                authorI.setRole(author.getRole());
            } else {
                if (authorI.getRole() == RoleID.CUSTOMER.getRole()) {
                    String message = USER_CHANGE_ROLE_PROBLEM;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
                }
                if(authorI.getRole() == RoleID.ADMIN.getRole()
                        && (bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole())) {
                    String message =UNAUTHORIZED_OPERATION;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
                }
            }
            authorDto = updateAuthorWithOptionalBooks(authorI,author,username_log,auth);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException
                 | TransactionSystemException ex) {
            String message = AUTHOR_NULL_BAD_REQUEST +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorBookNotFound(message,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityExistsException
                 | DataIntegrityViolationException ex) {
            String message = "JPA: " + USER_CONFLICT+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        authorDto.setPassword(null);
        return authorDto;
    }

    private AuthorDto updateAuthorWithOptionalBooks(AuthorDto authorDto,Author author,String username,String auth) {
        try {
            Author authorI= convertNewForm(authorDto, Author.class);
            if(authorDto.getBooks() != null ) {
                if(!authorDto.getBooks().isEmpty()) {
                    if(author.getBooks() != null
                    && !author.getBooks().isEmpty()) {
                       // this.deleteAuthorBooks(author.getId(),auth);
                    }
                }
            } else {
                authorI.setBooks(author.getBooks());
                authorService.addAuthorOrUpdate(authorI);
                return convertNewForm(authorI,AuthorDto.class);
            }
            List<Book> books = new ArrayList<>(authorI.getBooks());
            authorI.setBooks(author.getBooks());
            authorI = authorService.addAuthorOrUpdate(authorI);
            //authorI = this.addAuthorBooks(authorI, books,username);
            authorDto = convertNewForm(authorI,AuthorDto.class);
            log.info(String.format(
                    LOG_INSERT
                    ,username
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,"user (" + username +") has been update this author (" + authorI.getUsername()+")"));
        } catch (IllegalArgumentException
                | NullPointerException ex) {
            String message = AUTHOR_NULL+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNullException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return authorDto;
    }

    public BookDto getAuthorBookByIsbn(long id, long isbn, String auth) throws AuthorGeneralException {
        BookDto bookDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookByIsbn(isbn);
            if(book == null){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.NO_CONTENT);
            }
            bookDto = convertNewForm(book,BookDto.class);
            String message ="user ("+username_log+") get author ("+author.getUsername()+") book ("+book.getIsbn()+")";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
       } catch (NullPointerException
                 | UnsupportedOperationException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDto;
    }

    public BookDto getAuthorBookByIsbn(String author_username, long isbn, String auth) throws AuthorGeneralException {
        BookDto bookDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookByIsbn(isbn);
            if(book == null){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.NO_CONTENT);
            }
            bookDto = convertNewForm(book,BookDto.class);
            String message ="user ("+username_log+") get author ("+author.getUsername()+") book ("+book.getIsbn()+")";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException
                 | UnsupportedOperationException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDto;
    }

    public BookDto getAuthorBookById(long id, long bookId, String auth) throws AuthorGeneralException {
        BookDto bookDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookById(bookId);
            if(book == null){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.NO_CONTENT);
            }
            bookDto = convertNewForm(book,BookDto.class);
            String message ="user ("+username_log+") get author ("+author.getUsername()+") book ("+book.getIsbn()+")";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException
                 | UnsupportedOperationException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDto;
    }

    public BookDto getAuthorBookById(String author_username, long bookId, String auth) throws AuthorGeneralException {
        BookDto bookDto;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookById(bookId);
            if(book == null){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.NO_CONTENT);
            }
            bookDto = convertNewForm(book,BookDto.class);
            String message ="user ("+username_log+") get author ("+author.getUsername()+") book ("+book.getIsbn()+")";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException
                 | UnsupportedOperationException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return bookDto;
    }

    public BookDto updateAuthorBookByIsbn(long id, long isbn, BookDto bookDto, String auth) throws AuthorGeneralException {
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if (username == null
                    || username.isEmpty()) {
                String message = BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        , auth
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(BAD_CREDENTIAL, HttpStatus.BAD_REQUEST);
            }
            if (!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message = USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(message, HttpStatus.BAD_REQUEST);
            }
            if (bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || bookShopUserUtil.getUserByUsername(username).getId() != id)) {
                String message = UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(message, HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if (author == null) {
                String message = AUTHOR_NOT_EXISTS + " \\ " + BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorNotFoundException(message, HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookByIsbn(isbn);
            if (book == null) {
                String message = BOOK_NOT_FOUND + " \\ " + BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorBookNotFound(message, HttpStatus.NO_CONTENT);
            }
            book = updateAuthorBook(author,book,convertNewForm(bookDto,Book.class),username_log);
            bookDto = convertNewForm(book,BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return  bookDto;
    }

    public BookDto updateAuthorBookByIsbn(String author_username, long isbn, BookDto bookDto, String auth) throws AuthorGeneralException {
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if (username == null
                    || username.isEmpty()) {
                String message = BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        , auth
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(BAD_CREDENTIAL, HttpStatus.BAD_REQUEST);
            }
            if (!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message = USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(message, HttpStatus.BAD_REQUEST);
            }
            if (bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || !bookShopUserUtil.getUserByUsername(username).getId().equals(author_username))) {
                String message = UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(message, HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if (author == null) {
                String message = AUTHOR_NOT_EXISTS + " \\ " + BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorNotFoundException(message, HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookByIsbn(isbn);
            if (book == null) {
                String message = BOOK_NOT_FOUND + " \\ " + BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorBookNotFound(message, HttpStatus.NO_CONTENT);
            }
            book = updateAuthorBook(author,book,convertNewForm(bookDto,Book.class),username_log);
            bookDto = convertNewForm(book,BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return  bookDto;
    }

    public BookDto updateAuthorBookById(long id, long bookId, BookDto bookDto, String auth) throws AuthorGeneralException {
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if (username == null
                    || username.isEmpty()) {
                String message = BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        , auth
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(BAD_CREDENTIAL, HttpStatus.BAD_REQUEST);
            }
            if (!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message = USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(message, HttpStatus.BAD_REQUEST);
            }
            if (bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || bookShopUserUtil.getUserByUsername(username).getId() != id)) {
                String message = UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(message, HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if (author == null) {
                String message = AUTHOR_NOT_EXISTS + " \\ " + BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorNotFoundException(message, HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookById(bookId);
            if (book == null) {
                String message = BOOK_NOT_FOUND + " \\ " + BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorBookNotFound(message, HttpStatus.NO_CONTENT);
            }
            book = updateAuthorBook(author,book,convertNewForm(bookDto,Book.class),username_log);
            bookDto = convertNewForm(book,BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return  bookDto;

    }

    public BookDto updateAuthorBookById(String author_username, long bookId, BookDto bookDto, String auth) throws AuthorGeneralException {
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if (username == null
                    || username.isEmpty()) {
                String message = BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        , auth
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(BAD_CREDENTIAL, HttpStatus.BAD_REQUEST);
            }
            if (!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message = USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(message, HttpStatus.BAD_REQUEST);
            }
            if (bookShopUserUtil.getUserRole(username) != RoleID.ADMIN.getRole()
                    && (bookShopUserUtil.getUserRole(username) != RoleID.AUTHOR.getRole()
                    || !bookShopUserUtil.getUserByUsername(username).getId().equals(author_username))) {
                String message = UNAUTHORIZED_OPERATION;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorGeneralException(message, HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if (author == null) {
                String message = AUTHOR_NOT_EXISTS + " \\ " + BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorNotFoundException(message, HttpStatus.BAD_REQUEST);
            }
            Book book = author.getBookById(bookId);
            if (book == null) {
                String message = BOOK_NOT_FOUND + " \\ " + BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        , username_log
                        , DATE_FORMAT.format(new Date())
                        , this.getClass().getName()
                        , "WARN"
                        , message));
                throw new AuthorBookNotFound(message, HttpStatus.NO_CONTENT);
            }
            book = updateAuthorBook(author,book,convertNewForm(bookDto,Book.class),username_log);
            bookDto = convertNewForm(book,BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message =AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorNotFoundException(message);
        }
        return  bookDto;
    }

    private Book updateAuthorBook(Author author, Book book, Book updatedBook, String username_log) {
        try {
            if(updatedBook.getId() != Integer.MIN_VALUE
                    && updatedBook.getId() != 0
                    && updatedBook.getId() != book.getId()) {
                String message = BOOK_ID_CHANGE_ERROR;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            updatedBook.setId(book.getId());
            if(updatedBook.getIsbn() == Integer.MIN_VALUE
                    || updatedBook.getIsbn() == 0
                    || updatedBook.getIsbn() == book.getIsbn()) {
                updatedBook.setIsbn(book.getIsbn());
            } else {
                if((bookService.getBookByIsbn(updatedBook.getIsbn()) != null)) {
                    String message = BOOK_EXISTS_BAD_REQUEST +" \\ "+BAD_REQUEST;
                    log.warn(String.format(
                            LOG_INSERT
                            ,username_log
                            ,DATE_FORMAT.format(new Date())
                            ,this.getClass().getName()
                            ,"WARN"
                            ,message));
                    throw new BookExistsException(message,HttpStatus.CONFLICT);
                }
            }
            if(updatedBook.getAuthor() == null
                    || updatedBook.getAuthor().getId() == book.getAuthor().getId()) {
                updatedBook.setAuthor(book.getAuthor());
            } else {
                String message = BOOK_AUTHOR_CHANGE_ERROR +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            if(updatedBook.getCustomer() == null
                    || updatedBook.getCustomer().getId() == book.getCustomer().getId()) {
                if(updatedBook.getCustomer()!= null) {
                    updatedBook.setCustomer(book.getCustomer());
                }
            } else {
                String message = BOOK_CUSTOMER_CHANGE_ERROR +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            bookService.updateBook(updatedBook);
        } catch (IllegalArgumentException ex) {
            String message = BOOK_NULL+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (NullPointerException
                 | TransactionSystemException ex) {
            String message = AUTHOR_BOOK_NULL+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorBookNotFound(message,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityExistsException
                 | DataIntegrityViolationException ex) {
            String message = "JPA: " + BOOK_EXISTS+" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return updatedBook;
    }

    public List<BookDto> getAuthorBooksByName(long id, String name, String auth) throws AuthorGeneralException {
        List<BookDto> bookDtoList;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            List<Book> books =  author.getBooks(book -> book.getName().equals(name));
            if(books.isEmpty()){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.NO_CONTENT);
            }
            bookDtoList = mapListForm(books,BookDto.class);
            String books_author = "";
            for (Book book : books) {
                books_author += book.getName() + " : " + book.getIsbn() + " / ";
            }
            String message ="user ("+username_log+") get author ("+author.getUsername()+") books ("+books_author+")";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }  catch (NullPointerException
                 |  IllegalStateException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (UnsupportedOperationException
                | ClassCastException ex) {
            String message =BOOK_LIST_PROBLEM + " \\ " + SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }
        return bookDtoList;
    }

    public List<BookDto> getAuthorBooksByName(String author_username, String name, String auth) throws AuthorGeneralException {
        List<BookDto> bookDtoList;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            List<Book> books =  author.getBooks(book -> book.getName().equals(name));
            if(books.isEmpty()){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.NO_CONTENT);
            }
            bookDtoList = mapListForm(books,BookDto.class);
            String books_author = "";
            for (Book book : books) {
                books_author += book.getName() + " : " + book.getIsbn() + " / ";
            }
            String message ="user ("+username_log+") get author ("+author.getUsername()+") books ("+books_author+")";
            log.info(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"INFO"
                    ,message));
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }  catch (NullPointerException
                  |  IllegalStateException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        } catch (UnsupportedOperationException
                 | ClassCastException ex) {
            String message =BOOK_LIST_PROBLEM + " \\ " + SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }
        return bookDtoList;
    }

    public List<BookDto> getAuthorBooks(long id, String auth) throws AuthorGeneralException {
        List<BookDto> bookDtoList;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorById(id);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            List<Book> books = author.getBooks();
            if(books.isEmpty()){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.NO_CONTENT);
            }
            bookDtoList = mapListForm(books,BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_ID_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }  catch (NullPointerException
                  |  IllegalStateException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }
        return bookDtoList;
    }

    public List<BookDto> getAuthorBooks(String author_username, String auth) throws AuthorGeneralException {
        List<BookDto> bookDtoList;
        String username_log = "";
        try {
            String username = jwtUtil.getUserName(auth);
            username_log = username;
            if(username == null
                    || username.isEmpty()) {
                String message =BAD_CREDENTIAL;
                log.warn(String.format(
                        LOG_INSERT
                        ,auth
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(BAD_CREDENTIAL,HttpStatus.BAD_REQUEST);
            }
            if(!bookShopUserUtil.getUserByUsername(username).isEnabled()) {
                String message =USER_DEACTIVATED;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorGeneralException(message,HttpStatus.BAD_REQUEST);
            }
            Author author = authorService.getAuthorByUsername(author_username);
            if(author == null){
                String message =AUTHOR_NOT_EXISTS +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorNotFoundException(message,HttpStatus.BAD_REQUEST);
            }
            List<Book> books = author.getBooks();
            if(books.isEmpty()){
                String message =BOOK_NOT_FOUND +" \\ "+BAD_REQUEST;
                log.warn(String.format(
                        LOG_INSERT
                        ,username_log
                        ,DATE_FORMAT.format(new Date())
                        ,this.getClass().getName()
                        ,"WARN"
                        ,message));
                throw new AuthorBookNotFound(message,HttpStatus.NO_CONTENT);
            }
            bookDtoList = mapListForm(books,BookDto.class);
        } catch (IllegalArgumentException ex) {
            String message = AUTHOR_USERNAME_IS_NULL +" \\ "+SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }  catch (NullPointerException
                  |  IllegalStateException ex) {
            String message =SERVER_ERROR;
            log.error(String.format(
                    LOG_INSERT
                    ,username_log
                    ,DATE_FORMAT.format(new Date())
                    ,this.getClass().getName()
                    ,"ERROR"
                    ,message));
            throw new AuthorGeneralException(message);
        }
        return bookDtoList;
    }

//    public  AuthorDto updateAuthor(long id,JsonPatch patch) {
//        try {
//            Author author = authorService.getAuthorById(id);
//            if(author == null) {
//                throw  new AuthorBookNotFound();
//            }
//            Author authorPatched = applyPatchToAuthor(patch, author);
//            return convertNewForm( authorService.addAuthorOrUpdate(authorPatched),AuthorDto.class);
//        } catch (JsonPatchException | JsonProcessingException e) {
//            throw new AuthorGeneralException("Patch Exception");
//        } catch (NullPointerException |
//                IllegalArgumentException e) {
//            throw new AuthorNotFoundException();
//        }
//    }

//    private Author applyPatchToAuthor(
//            JsonPatch patch, Author targetAuthor) throws JsonPatchException, JsonProcessingException {
//        JsonNode patched = patch.apply(mapper.convertForm(targetAuthor, JsonNode.class));
//        return mapper.convertForm(patched, Author.class);
//    }

//    // map methods
    private <P,T> List<T> mapListForm(List<P> p, Class<T> dest) {
        List<T>  t;
        try {
            t = p.stream()
                    .map(pi-> mapper.convertForm(pi,dest))
                    .collect(Collectors.toList());
        } catch (ConfigurationException
                | IllegalArgumentException
                | MappingException ex) {
            throw new AuthorGeneralException(MODEL_MAPPER_ILLEGAL_EXCEPTION
                    +" \\ "+SERVER_ERROR);
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
            throw new AuthorGeneralException(MODEL_MAPPER_ILLEGAL_EXCEPTION
                    +" \\ "+SERVER_ERROR);
        }
        return t;
    }
}
