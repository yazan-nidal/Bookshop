package exp.exalt.bookshop.controllers;

import exp.exalt.bookshop.dto.AuthorDto;
import exp.exalt.bookshop.util.AuthorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = {"/",""})
    public ResponseEntity<Object> addAuthor(@RequestBody AuthorDto author) {
        authorUtil.addAuthor(author);
        return new ResponseEntity<>("Author is created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteAuthorById(@PathVariable(value = "id") long id) {
        authorUtil.deleteAuthor(id);
        return new ResponseEntity<>("Author deleted successfully", HttpStatus.ACCEPTED);
    }

}
