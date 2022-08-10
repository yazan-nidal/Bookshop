package exp.exalt.bookshop.util;

import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.dto.author_dto.AuthorDto;
import exp.exalt.bookshop.dto.author_dto.AuthorDtoO;
import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.dto.book_dto.BookDtoO;
import exp.exalt.bookshop.dto.customer_dto.CustomerDto;
import exp.exalt.bookshop.exceptions.author_exceptions.AuthorBookNull;
import exp.exalt.bookshop.exceptions.author_exceptions.AuthorGeneralException;
import exp.exalt.bookshop.exceptions.author_exceptions.AuthorNotFoundException;
import exp.exalt.bookshop.exceptions.book_exceptions.BookExistsException;
import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.models.BookShopUser;
import exp.exalt.bookshop.models.Customer;
import exp.exalt.bookshop.services.AuthorService;
import exp.exalt.bookshop.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorUtilTest {
    @InjectMocks
    AuthorUtil authorUtil;

    @Mock
    AuthorService authorService;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    BookShopUserUtil bookShopUserUtil;
    @Mock
    BookService bookService;
    @Mock
    Mapper mapper;

    BookShopUser  bookShopUserD;
    BookShopUser  bookShopUserU;

    Author author;
    Author authorS;
    Book book;
    BookDto bookDto;

    AuthorDtoO authorDtoO;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        bookShopUserD = new BookShopUser(0L,"admin","admin","ahmad",1,false);
        bookShopUserU = new BookShopUser(240L,"admin","admin","ahmad",1,true);

        author = new Author();
        author.setBooks(new ArrayList<>());
        book = new Book(100,"book1",author,null,11);
        bookDto = new BookDto(100,"book1",new AuthorDto(),null,11);

        authorS = new Author();
        authorS.setBooks(new ArrayList<>());
        authorS.addBook(book);

        authorDtoO = new AuthorDtoO();
        authorDtoO.setBooks(new ArrayList<>());
        List<BookDtoO> bookDtoOList = new ArrayList<>();
        bookDtoOList.add(new BookDtoO());
        authorDtoO.setBooks(bookDtoOList);
    }

  // add Author Book tests
    @Test
     public void addAuthorBook_BadCredentialTest() {
        when(jwtUtil.getUserName(anyString())).thenReturn("");
        assertThrows(AuthorGeneralException.class,() -> authorUtil.addAuthorBook(230,"auth test",null));
    }

    @Test
    public void addAuthorBook_UserDeactivated_Test() {
        when(jwtUtil.getUserName(anyString())).thenReturn("admin");
        when(bookShopUserUtil.getUserByUsername("admin")).thenReturn(bookShopUserD);
        assertThrows(AuthorGeneralException.class,() -> authorUtil.addAuthorBook(230,"auth test",null));
    }

    @Test
    public void addAuthorBook_UnauthorizedUser_Test() {
        when(jwtUtil.getUserName(anyString())).thenReturn("admin");
        when(bookShopUserUtil.getUserRole("admin")).thenReturn(1);
        when(bookShopUserUtil.getUserByUsername("admin")).thenReturn(bookShopUserU);
        assertThrows(AuthorGeneralException.class,() -> authorUtil.addAuthorBook(230,"auth test",null));
    }

    @Test
    public void addAuthorBook_AuthorNotExists_Test() {
        when(jwtUtil.getUserName(anyString())).thenReturn("admin");
        when(bookShopUserUtil.getUserRole("admin")).thenReturn(1);
        when(bookShopUserUtil.getUserByUsername("admin")).thenReturn(bookShopUserU);
        when(authorService.getAuthorById(bookShopUserU.getId())).thenReturn(null);
        assertThrows(AuthorNotFoundException.class,() -> authorUtil.addAuthorBook(240,"auth test",null));
    }
    @Test
    public void addAuthorBook_AuthorBookNull_Test() {
        when(jwtUtil.getUserName(anyString())).thenReturn("admin");
        when(bookShopUserUtil.getUserRole("admin")).thenReturn(1);
        when(bookShopUserUtil.getUserByUsername("admin")).thenReturn(bookShopUserU);
        when(authorService.getAuthorById(bookShopUserU.getId())).thenReturn(author);
        assertThrows(AuthorBookNull.class,() -> authorUtil.addAuthorBook(240,"auth test",null));
    }

    @Test
    public void addAuthorBook_AuthorBookExists_Test() {
        when(jwtUtil.getUserName(anyString())).thenReturn("admin");
        when(bookShopUserUtil.getUserRole("admin")).thenReturn(1);
        when(bookShopUserUtil.getUserByUsername("admin")).thenReturn(bookShopUserU);
        when(authorService.getAuthorById(bookShopUserU.getId())).thenReturn(author);
        when(bookService.getBookByIsbn(bookDto.getIsbn())).thenReturn(book);
        assertThrows(BookExistsException.class,() -> authorUtil.addAuthorBook(240,"auth test",bookDto));
    }

    @Test
    public void addAuthorBook_ForceRentBookForCustomer_Test() {
        bookDto.setCustomer(new CustomerDto());
        when(jwtUtil.getUserName(anyString())).thenReturn("admin");
        when(bookShopUserUtil.getUserRole("admin")).thenReturn(1);
        when(bookShopUserUtil.getUserByUsername("admin")).thenReturn(bookShopUserU);
        when(authorService.getAuthorById(bookShopUserU.getId())).thenReturn(author);
        when(bookService.getBookByIsbn(bookDto.getIsbn())).thenReturn(null);
        assertThrows(AuthorGeneralException.class,() -> authorUtil.addAuthorBook(240,"auth test",bookDto));
    }
    
    @Test
    public void addAuthorBook_Success_Test() {
        when(jwtUtil.getUserName(anyString())).thenReturn("admin");
        when(bookShopUserUtil.getUserRole("admin")).thenReturn(1);
        when(bookShopUserUtil.getUserByUsername("admin")).thenReturn(bookShopUserU);
        when(authorService.getAuthorById(bookShopUserU.getId())).thenReturn(author);
        when(bookService.getBookByIsbn(bookDto.getIsbn())).thenReturn(null);
        when(mapper.convertForm(bookDto,Book.class)).thenReturn(book);
        when(authorService.addAuthorOrUpdate(author)).thenReturn(authorS);
        when(mapper.convertForm(authorS,AuthorDtoO.class)).thenReturn(authorDtoO);
        assertEquals(authorUtil.addAuthorBook(240,"auth test",bookDto).getBooks().size(),1);
    }
    
}
