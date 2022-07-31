package exp.exalt.bookshop.controllers;

import exp.exalt.bookshop.dto.book_dto.BookDto;
import exp.exalt.bookshop.dto.customer_dto.CustomerDto;
import exp.exalt.bookshop.util.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerUtil customerUtil;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Object> getAllCustomers() {
        return new ResponseEntity<>(customerUtil.getCustomers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(customerUtil.getCustomerById(id), HttpStatus.FOUND);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getCustomersByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(customerUtil.getCustomersByName(name), HttpStatus.FOUND);
    }

    @PostMapping(value = {"/",""})
    public ResponseEntity<Object> addCustomer(@RequestBody CustomerDto customer) {
        return new ResponseEntity<>(customerUtil.addCustomer(customer), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteCustomerById(@PathVariable(value = "id") long id) {
        customerUtil.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/books")
    public ResponseEntity<Object> rentBook(@PathVariable(value = "id") long id, @RequestBody BookDto book) {
        return  new ResponseEntity<>(customerUtil.rentCustomerBook(id,book), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}/books/{isbn}")
    public ResponseEntity<Object> returnBook(@PathVariable(value = "id") long id, @PathVariable(value = "isbn") long isbn) {
        return  new ResponseEntity<>(customerUtil.returnCustomerBook(id,isbn), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/books")
    public ResponseEntity<Object> returnAllBooks(@PathVariable(value = "id") long id) {
        return  new ResponseEntity<>(customerUtil.returnCustomerBooks(id), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/bookList")
    public ResponseEntity<Object> rentBooks(@PathVariable(value = "id") long id, @RequestBody List<BookDto> books) {
        return  new ResponseEntity<>(customerUtil.rentCustomerBooks(id,books), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/books/{isbn}")
    public ResponseEntity<Object> getCustomerBookByIsbn(@PathVariable(value = "id") long id, @PathVariable(value = "isbn") long  isbn) {
        return  new ResponseEntity<>(customerUtil.getCustomerBookByIsbn(id,isbn), HttpStatus.FOUND);
    }

    @GetMapping(value = "/{id}/books/'{name}'")
    public ResponseEntity<Object> getCustomerBookByName(@PathVariable(value = "id") long id, @PathVariable(value = "name") String name) {
        return  new ResponseEntity<>(customerUtil.getCustomerBooksByName(id,name), HttpStatus.FOUND);
    }

    @GetMapping(value = "/{id}/books")
    public ResponseEntity<Object> returnBooks(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(customerUtil.returnCustomerBooks(id), HttpStatus.FOUND);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable(value = "id") long id, @RequestBody CustomerDto customer) {
        return  new ResponseEntity<>(customerUtil.updateCustomer(id,customer), HttpStatus.OK);
    }
}
