package exp.exalt.bookshop.controller;

import exp.exalt.bookshop.model.Author;
import exp.exalt.bookshop.model.Book;
import exp.exalt.bookshop.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/authors")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @GetMapping(value = {"/",""})
    public ResponseEntity<Object>  getAllAuthors() {
        return new ResponseEntity<>(authorService.getAuthors(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getAuthorById(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(authorService.getAuthorById(id), HttpStatus.FOUND);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getAuthorByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(authorService.getAuthorByName(name), HttpStatus.FOUND);
    }

    @PostMapping(value = {"/",""})
    public ResponseEntity<Object> addAuthor(@RequestBody Author author) {
        authorService.addAuthor(author);
        return new ResponseEntity<>("Author is created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteAuthorById(@PathVariable(value = "id") long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>("Author deleted successfully", HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/{id}/books")
    public ResponseEntity<Object> addBook(@PathVariable(value = "id") long id, @RequestBody Book book) {
        return new ResponseEntity<>(authorService.addBook(id,book), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/books")
    public ResponseEntity<Object> getAuthorBooks(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(authorService.getBooks(id), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/books/{isbn}")
    public ResponseEntity<Object> getAuthorBook(@PathVariable(value = "id") long id, @PathVariable(value = "isbn")long isbn) {
        return new ResponseEntity<>(authorService.getBook(id, isbn), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/books/{isbn}")
    public ResponseEntity<Object> deleteAuthorBook(@PathVariable(value = "id") long id, @PathVariable(value = "isbn")long isbn) {
        authorService.deleteBook(id, isbn);
        return new ResponseEntity<>("Author delete his Book resource successfully", HttpStatus.ACCEPTED);
    }
}
