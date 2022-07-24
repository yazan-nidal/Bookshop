package exp.exalt.bookshop.controller;

import exp.exalt.bookshop.model.Author;
import exp.exalt.bookshop.model.Book;
import exp.exalt.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = {"/",""})
    public ResponseEntity<Object> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping(value = "/{isbn}")
    public ResponseEntity<Object> getBookById(@PathVariable(value = "isbn") long isbn) {
        return new ResponseEntity<>(bookService.getBookByIsbn(isbn), HttpStatus.FOUND);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getBookByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(bookService.getBookByName(name), HttpStatus.FOUND);
    }

    @PostMapping(value = {"/",""})
    public ResponseEntity<Object> addBook(@RequestBody Book book) {
        bookService.addBook(book);
        return new ResponseEntity<>("Book is created successfully", HttpStatus.CREATED);
    }

//    @PutMapping(value = "/books/{id}")
//    public void updateBook(@RequestBody Book book, @PathVariable("id") long id) { bookService.updateBook(book, id); }
//
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable("id") long id){
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book resource deleted successfully", HttpStatus.ACCEPTED);
    }

}
