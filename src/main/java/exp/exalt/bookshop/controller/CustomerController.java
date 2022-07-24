package exp.exalt.bookshop.controller;

import exp.exalt.bookshop.model.Author;
import exp.exalt.bookshop.model.Book;
import exp.exalt.bookshop.model.Customer;
import exp.exalt.bookshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping(value = {"/",""})
    public ResponseEntity<Object> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(customerService.getCustomerById(id), HttpStatus.FOUND);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getCustomerByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(customerService.getCustomerByName(name), HttpStatus.FOUND);
    }

    @PostMapping(value = {"/",""})
    public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return new ResponseEntity<>("Customer is created successfully", HttpStatus.CREATED);
    }

//    @PutMapping(value = "/customers/{id}")
//    public void updateCustomer(@RequestBody Customer customer, @PathVariable("id") long id){ customerService.updateCustomer(customer,id); }
//
//    @DeleteMapping(value = "/customers/{id}")
//    public void deleteCustomer(@PathVariable("id") long id){
//        customerService.deleteCustomer(id);
//    }
}
