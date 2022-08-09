package exp.exalt.bookshop.controllers;

import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.dto.author_dto.AuthorDto;
import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.models.Book;
import exp.exalt.bookshop.repositories.BookRepository;
import exp.exalt.bookshop.util.AuthorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/authors")
public class AuthorController {
    @Autowired
    AuthorUtil authorUtil;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    Mapper mapper;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Object> getAllAuthors(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return new ResponseEntity<>(authorUtil.getAuthors(auth), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getAuthorById(@PathVariable(value = "id") long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return new ResponseEntity<>(authorUtil.getAuthorById(id,auth), HttpStatus.OK);
    }

    @GetMapping(value = "/author/{username}")
    public ResponseEntity<Object> getAuthorById(@PathVariable(value = "username") String username, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return new ResponseEntity<>(authorUtil.getAuthorByUsername(username,auth), HttpStatus.OK);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getAuthorsByName(@PathVariable(value = "name") String name, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return new ResponseEntity<>(authorUtil.getAuthorsByName(name,auth), HttpStatus.OK);
    }

    @PostMapping(value = {"/create/author"})
    public ResponseEntity<Object> addAuthor(@RequestBody AuthorDto author, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return new ResponseEntity<>(authorUtil.addAuthor(author, auth), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteAuthorById(@PathVariable(value = "id") long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return new ResponseEntity<>(authorUtil.deleteAuthor(id, auth),HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/delete/{username}")
    public ResponseEntity<Object> deleteAuthorByUsername(@PathVariable(value = "username") String username, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return new ResponseEntity<>(authorUtil.deleteAuthor(username, auth),HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/{id}/books")
    public ResponseEntity<Object> addBook(@PathVariable(value = "id") long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody BookDto book) {

        //AuthorDto a = authorUtil.getAuthorById(id,auth);
      //  book.setAuthor(a);
        //Book b = bookRepository.save(mapper.convertForm(book, Book.class));
       // new ResponseEntity<>(authorUtil.addAuthorBook(id,auth,book), HttpStatus.CREATED);
        return  new ResponseEntity<>(authorUtil.addAuthorBook(id,auth,book), HttpStatus.CREATED);
    }

    @PostMapping(value = "/author/{username}/books")
    public ResponseEntity<Object> addBook(@PathVariable(value = "username") String username, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody BookDto book) {
        return  new ResponseEntity<>(authorUtil.addAuthorBook(username,auth,book), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/bookList")
    public ResponseEntity<Object> addAuthorBooks(@PathVariable(value = "id") long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody List<BookDto> books) {
        return  new ResponseEntity<>(authorUtil.addAuthorBooks(id,books,auth), HttpStatus.CREATED);
    }

    @PostMapping(value = "/author/{username}/bookList")
    public ResponseEntity<Object> addAuthorBooks(@PathVariable(value = "username") String username, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody List<BookDto> books) {
        return  new ResponseEntity<>(authorUtil.addAuthorBooks(username,books,auth), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/books/{isbn}")
    public ResponseEntity<Object> deleteBookByIsbn(@PathVariable(value = "id") long id, @PathVariable(value = "isbn") long isbn, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.deleteAuthorBookByIsbn(id,isbn,auth), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/author/{username}/books/{isbn}")
    public ResponseEntity<Object> deleteBookByIsbn(@PathVariable(value = "username") String username, @PathVariable(value = "isbn") long isbn, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.deleteAuthorBookByIsbn(username,isbn,auth), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/author/{username}/books/book/{book_id}")
    public ResponseEntity<Object> deleteBookById(@PathVariable(value = "username") String username, @PathVariable(value = "book_id") long book_id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.deleteAuthorBookById(username,book_id,auth), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}/books/book/{book_id}")
    public ResponseEntity<Object> deleteBookById(@PathVariable(value = "id") long id, @PathVariable(value = "book_id") long book_id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.deleteAuthorBookById(id,book_id,auth), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}/books")
    public ResponseEntity<Object> deleteAllBooks(@PathVariable(value = "id") long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.deleteAuthorBooks(id,auth), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/author/{username}/books")
    public ResponseEntity<Object> deleteAllBooks(@PathVariable(value = "username") String  username, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.deleteAuthorBooks(username,auth), HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateAuthor(@PathVariable(value = "id") long id, @RequestBody AuthorDto author, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.updateAuthor(id,author, auth), HttpStatus.OK);
    }

    @PutMapping(value = "/author/{username}")
    public ResponseEntity<Object> updateAuthor(@PathVariable(value = "username") String username, @RequestBody AuthorDto author, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>( authorUtil.updateAuthor(username,author, auth), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/books/{isbn}")
    public ResponseEntity<Object> getAuthorBookByIsbn(@PathVariable(value = "id") long id, @PathVariable(value = "isbn") long  isbn, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.getAuthorBookByIsbn(id,isbn,auth), HttpStatus.OK);
    }

    @GetMapping(value = "/author/{username}/books/{isbn}")
    public ResponseEntity<Object> getAuthorBookByIsbn(@PathVariable(value = "username") String username, @PathVariable(value = "isbn") long  isbn, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.getAuthorBookByIsbn(username,isbn,auth), HttpStatus.OK);
    }

    @GetMapping(value = "/author/{username}/books/book/{book_id}")
    public ResponseEntity<Object> getAuthorBookById(@PathVariable(value = "username") String username, @PathVariable(value = "book_id") long  book_id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.getAuthorBookById(username,book_id,auth), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/books/book/{book_id}")
    public ResponseEntity<Object> getAuthorBookById(@PathVariable(value = "id") long id, @PathVariable(value = "book_id") long  book_id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.getAuthorBookById(id,book_id,auth), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/books/'{name}'")
    public ResponseEntity<Object> getAuthorBookByName(@PathVariable(value = "id") long id, @PathVariable(value = "name") String name, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.getAuthorBooksByName(id,name,auth), HttpStatus.OK);
    }

    @GetMapping(value = "/author/{username}/books/'{name}'")
    public ResponseEntity<Object> getAuthorBookByName(@PathVariable(value = "username") String username, @PathVariable(value = "name") String name, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return  new ResponseEntity<>(authorUtil.getAuthorBooksByName(username,name,auth), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/books")
    public ResponseEntity<Object> getAuthorBooks(@PathVariable(value = "id") long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return new ResponseEntity<>(authorUtil.getAuthorBooks(id,auth), HttpStatus.FOUND);
    }

    @GetMapping(value = "/author/{username}/books")
    public ResponseEntity<Object> getAuthorBooks(@PathVariable(value = "username") String username, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return new ResponseEntity<>(authorUtil.getAuthorBooks(username,auth), HttpStatus.FOUND);
    }

//    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
//    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable long id, @RequestBody JsonPatch patch) {
//        return  new ResponseEntity<>(authorUtil.updateAuthor(id,patch), HttpStatus.OK);
//    }
}
