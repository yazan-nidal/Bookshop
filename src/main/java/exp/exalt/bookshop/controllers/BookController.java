package exp.exalt.bookshop.controllers;

import exp.exalt.bookshop.dto.BookDto;
import exp.exalt.bookshop.util.BookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    BookUtil bookUtil;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Object> getAllBooks() {
        return new ResponseEntity<>(bookUtil.getBooks(), HttpStatus.OK);
    }

    @GetMapping(value = "/{isbn}")
    public ResponseEntity<Object> getBookByIsbn(@PathVariable(value = "isbn") long isbn) {
        return new ResponseEntity<>(bookUtil.getBookByIsbn(isbn), HttpStatus.FOUND);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getBooksByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(bookUtil.getBooksByName(name), HttpStatus.FOUND);
    }

    @PostMapping(
            value = {"/",""},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = "application/json; charset=utf8")
    public ResponseEntity<Object> addBook(@RequestBody BookDto book)  {
             bookUtil.addBook(book);
        return new ResponseEntity<>("Book is created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{isbn}")
    public ResponseEntity<Object> deleteBookByIsbn(@PathVariable(value = "isbn") long isbn) {
       bookUtil.deleteBook(isbn);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.ACCEPTED);
    }

}
