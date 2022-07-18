package exp.exalt.bookshop.controller;

import exp.exalt.bookshop.model.Book;
import exp.exalt.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable("id") long id) {
        return bookService.getBook(id);
    }

    @PostMapping(value = "/books")
    public void addBook(@RequestBody Book book){
        bookService.addBook(book);
    }

    @PutMapping(value = "/books/{id}")
    public void updateBook(@RequestBody Book book, @PathVariable("id") long id){
        bookService.updateBook(book, id);
    }

    @DeleteMapping(value = "/books/{id}")
    public void deleteBook(@PathVariable("id") long id){
        bookService.deleteBook(id);
    }

}
