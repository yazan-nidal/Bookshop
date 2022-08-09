package exp.exalt.bookshop.controllers;

import exp.exalt.bookshop.dto.customer_dto.CustomerDto;
import exp.exalt.bookshop.util.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customers")
public class CustomerControllers {
  @Autowired
    CustomerUtil customerUtil;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Object> getAllCustomers() {
        return new ResponseEntity<>(customerUtil.getCustomers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(customerUtil.getCustomerById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getCustomersByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(customerUtil.getCustomersByName(name), HttpStatus.OK);
    }

    @GetMapping(value = "/customer/{username}")
    public ResponseEntity<Object> getCustomersByUsername(@PathVariable(value = "username") String username) {
        return new ResponseEntity<>(customerUtil.getCustomerByUsername(username), HttpStatus.OK);
    }

    @PostMapping(value = {"/create/customer"})
    public ResponseEntity<Object> addCustomer(@RequestBody CustomerDto customer) {
        return new ResponseEntity<>( customerUtil.addCustomer(customer), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteCustomerById(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(customerUtil.deleteCustomer(id),HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/delete/{username}")
    public ResponseEntity<Object> deleteCustomerByUsername(@PathVariable(value = "username") String username) {
        return new ResponseEntity<>(customerUtil.deleteCustomer(username),HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/books/rent/{isbn}")
    public ResponseEntity<Object> rentBookByIsbn(@PathVariable(value = "id") long id, @PathVariable(value = "isbn") long isbn) {
        return  new ResponseEntity<>(customerUtil.rentCustomerBookByIsbn(id,isbn), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/books/rent/book/{book_id}")
    public ResponseEntity<Object> rentBookById(@PathVariable(value = "id") long id, @PathVariable(value = "book_id") long book_id) {
        return  new ResponseEntity<>(customerUtil.rentCustomerBookById(id,book_id), HttpStatus.OK);
    }

    @GetMapping(value = "/customer/{username}/books/rent/{isbn}")
    public ResponseEntity<Object> rentBookByIsbn(@PathVariable(value = "username") String username, @PathVariable(value = "isbn") long isbn) {
        return  new ResponseEntity<>(customerUtil.rentCustomerBookByIsbn(username,isbn), HttpStatus.OK);
    }

    @GetMapping(value = "/customer/{username}/books/rent/book/{book_id}")
    public ResponseEntity<Object> rentBookById(@PathVariable(value = "username") String username, @PathVariable(value = "book_id") long book_id) {
        return  new ResponseEntity<>(customerUtil.rentCustomerBookById(username,book_id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/books/return/{isbn}")
    public ResponseEntity<Object> returnCustomerBookByIsbn(@PathVariable(value = "id") long id, @PathVariable(value = "isbn") long isbn) {
        return  new ResponseEntity<>(customerUtil.returnCustomerBookByIsbn(id,isbn), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}/books/return/book/{book_id}")
    public ResponseEntity<Object> returnCustomerBookById(@PathVariable(value = "id") long id, @PathVariable(value = "book_id") long book_id) {
        return  new ResponseEntity<>(customerUtil.returnCustomerBookById(id,book_id), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/customer/{username}/books/return/{isbn}")
    public ResponseEntity<Object> returnCustomerBookByIsbn(@PathVariable(value = "username") String username, @PathVariable(value = "isbn") long isbn) {
        return  new ResponseEntity<>(customerUtil.returnCustomerBookByIsbn(username,isbn), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/customer/{username}/books/return/book/{book_id}")
    public ResponseEntity<Object> returnCustomerBookById(@PathVariable(value = "username") String username, @PathVariable(value = "book_id") long book_id) {
        return  new ResponseEntity<>(customerUtil.returnCustomerBookById(username,book_id), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}/books")
    public ResponseEntity<Object> returnAllCustomerBooks(@PathVariable(value = "id") long id) {
        return  new ResponseEntity<>(customerUtil.returnCustomerBooks(id), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/customer/{username}/books")
    public ResponseEntity<Object> returnAllCustomerBooks(@PathVariable(value = "username") String username) {
        return  new ResponseEntity<>(customerUtil.returnCustomerBooks(username), HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable(value = "id") long id, @RequestBody CustomerDto customer) {
        return  new ResponseEntity<>(customerUtil.updateCustomer(id,customer), HttpStatus.OK);
    }

    @PutMapping(value = "/customer/{username}")
    public ResponseEntity<Object> updateCustomer(@PathVariable(value = "username") String username, @RequestBody CustomerDto customer) {
        return  new ResponseEntity<>(customerUtil.updateCustomer(username,customer), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/books/{isbn}")
    public ResponseEntity<Object> getCustomerBookByIsbn(@PathVariable(value = "id") long id, @PathVariable(value = "isbn") long  isbn) {
        return new ResponseEntity<>(customerUtil.getCustomerBookByIsbn(id,isbn), HttpStatus.OK);
    }
    @GetMapping(value = "/customer{username}/books/{isbn}")
    public ResponseEntity<Object> getCustomerBookByIsbn(@PathVariable(value = "username") String username, @PathVariable(value = "isbn") long  isbn) {
        return new ResponseEntity<>(customerUtil.getCustomerBookByIsbn(username,isbn), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/books/book/{book_id}")
    public ResponseEntity<Object> getCustomerBookById(@PathVariable(value = "id") long id, @PathVariable(value = "book_id") long  book_id) {
        return new ResponseEntity<>(customerUtil.getCustomerBookById(id,book_id), HttpStatus.OK);
    }

    @GetMapping(value = "/customer{username}/books/book/{book_id}")
    public ResponseEntity<Object> getCustomerBookById(@PathVariable(value = "username") String username, @PathVariable(value = "book_id") long  book_id) {
        return new ResponseEntity<>(customerUtil.getCustomerBookById(username,book_id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/books/'{name}'")
    public ResponseEntity<Object> getCustomerBooksByName(@PathVariable(value = "id") long id, @PathVariable(value = "name") String name) {
        return  new ResponseEntity<>(customerUtil.getCustomerBooksByName(id,name), HttpStatus.OK);
    }

    @GetMapping(value = "/customer{username}/books/'{name}'")
    public ResponseEntity<Object> getCustomerBooksByName(@PathVariable(value = "username") String username, @PathVariable(value = "name") String name) {
        return  new ResponseEntity<>(customerUtil.getCustomerBooksByName(username,name), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/books")
    public ResponseEntity<Object> getCustomerBooks(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(customerUtil.getCustomerBooks(id), HttpStatus.FOUND);
    }

    @GetMapping(value = "/customer/{username}/books")
    public ResponseEntity<Object> getCustomerBooks(@PathVariable(value = "username") String username) {
        return new ResponseEntity<>(customerUtil.getCustomerBooks(username), HttpStatus.FOUND);
    }
}
