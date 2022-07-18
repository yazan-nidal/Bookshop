package exp.exalt.bookshop.controller.book;

import exp.exalt.bookshop.model.Book;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class BookController {

    @RequestMapping("/books")
    public List<Book> getAllBooks() {
        return Arrays.asList(
                new Book(11200,"Code Complete",10100),
                new Book(11201,"Clean Code",10101),
                new Book(11202,"The Little Schemer",10102)
        );
    }


}
