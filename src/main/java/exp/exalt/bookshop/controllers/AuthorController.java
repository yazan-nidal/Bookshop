package exp.exalt.bookshop.controllers;

import com.github.fge.jsonpatch.JsonPatch;
import exp.exalt.bookshop.dto.author_dto.AuthorDto;
import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.util.AuthorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/authors")
public class AuthorController {
    @Autowired
    AuthorUtil authorUtil;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Object> getAllAuthors() {
        return new ResponseEntity<>(authorUtil.getAuthors(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getAuthorById(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(authorUtil.getAuthorById(id), HttpStatus.FOUND);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getAuthorsByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(authorUtil.getAuthorsByName(name), HttpStatus.FOUND);
    }

    @PostMapping(value = {"/create/author"})
    public ResponseEntity<Object> addAuthor(@RequestBody AuthorDto author) {
        return new ResponseEntity<>( authorUtil.addAuthor(author), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteAuthorById(@PathVariable(value = "id") long id) {
        authorUtil.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/books")
    public ResponseEntity<Object> addBook(@PathVariable(value = "id") long id, @RequestBody BookDto book) {
        return  new ResponseEntity<>(authorUtil.addAuthorBook(id,book), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/books/{isbn}")
    public ResponseEntity<Object> deleteBook(@PathVariable(value = "id") long id, @PathVariable(value = "isbn") long isbn) {
        return  new ResponseEntity<>(authorUtil.deleteAuthorBook(id,isbn), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/books")
    public ResponseEntity<Object> deleteAllBooks(@PathVariable(value = "id") long id) {
        return  new ResponseEntity<>(authorUtil.deleteAuthorBooks(id), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/bookList")
    public ResponseEntity<Object> addAuthorBooks(@PathVariable(value = "id") long id, @RequestBody List<BookDto> books) {
        return  new ResponseEntity<>(authorUtil.addAuthorBooks(id,books), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/books/{isbn}")
    public ResponseEntity<Object> getAuthorBookByIsbn(@PathVariable(value = "id") long id, @PathVariable(value = "isbn") long  isbn) {
        return  new ResponseEntity<>(authorUtil.getAuthorBookByIsbn(id,isbn), HttpStatus.FOUND);
    }

    @GetMapping(value = "/{id}/books/'{name}'")
    public ResponseEntity<Object> getAuthorBookByName(@PathVariable(value = "id") long id, @PathVariable(value = "name") String name) {
        return  new ResponseEntity<>(authorUtil.getAuthorBooksByName(id,name), HttpStatus.FOUND);
    }

    @GetMapping(value = "/{id}/books")
    public ResponseEntity<Object> getAuthorBooks(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(authorUtil.getAuthorBooks(id), HttpStatus.FOUND);
    }

        @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateAuthor(@PathVariable(value = "id") long id, @RequestBody AuthorDto author) {
        return  new ResponseEntity<>(authorUtil.updateAuthor(id,author), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable long id, @RequestBody JsonPatch patch) {
        return  new ResponseEntity<>(authorUtil.updateAuthor(id,patch), HttpStatus.OK);
    }
}
