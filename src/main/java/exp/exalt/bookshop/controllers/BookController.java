package exp.exalt.bookshop.controllers;

import exp.exalt.bookshop.util.BookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping(value = "/book/{book_id}")
    public ResponseEntity<Object> getBooksByName(@PathVariable(value = "book_id") long book_id) {
        return new ResponseEntity<>(bookUtil.getBookById(book_id), HttpStatus.FOUND);
    }
}
