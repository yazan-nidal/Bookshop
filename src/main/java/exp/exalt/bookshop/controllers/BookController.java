package exp.exalt.bookshop.controllers;

import exp.exalt.bookshop.dto.BookDtoC;
import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.util.BookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    BookUtil bookUtil;
    @Autowired
    Mapper mapper;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Object> getAllBooks() {
        return new ResponseEntity<>(bookUtil.getBooks().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList())
                , HttpStatus.OK);
    }

    @GetMapping(value = "/{isbn}")
    public ResponseEntity<Object> getBookByIsbn(@PathVariable(value = "isbn") long isbn) {
        return new ResponseEntity<>(mapper.convertToDto(bookUtil.getBookByIsbn(isbn)), HttpStatus.FOUND);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getBooksByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(bookUtil.getBooksByName(name).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList())
                , HttpStatus.FOUND);
    }

    @PostMapping(value = {"/",""})
    public ResponseEntity<Object> addBook(@RequestBody BookDtoC bookDto)  {
             bookUtil.addBook(mapper.bookDtoToBook(bookDto));
        return new ResponseEntity<>("Book is created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{isbn}")
    public ResponseEntity<Object> deleteBookByIsbn(@PathVariable(value = "isbn") long isbn) {
       bookUtil.deleteBook(isbn);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.ACCEPTED);
    }

}
