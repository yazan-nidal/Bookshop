package exp.exalt.bookshop.controllers;

import exp.exalt.bookshop.dto.Mapper;
import exp.exalt.bookshop.dto.UserDto;
import exp.exalt.bookshop.util.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerUtil customerUtil;
    @Autowired
    Mapper mapper;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Object> getAllCustomers() {
        return new ResponseEntity<>(customerUtil.getCustomers().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList())
                , HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(mapper.convertToDto(customerUtil.getCustomerById(id)), HttpStatus.FOUND);
    }

    @GetMapping(value = "/'{name}'")
    public ResponseEntity<Object> getCustomersByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(customerUtil.getCustomersByName(name).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList())
                , HttpStatus.FOUND);
    }

    @PostMapping(value = {"/",""})
    public ResponseEntity<Object> addCustomer(@RequestBody UserDto userDto) {
        customerUtil.addCustomer(mapper.userToCustomer(userDto));
        return new ResponseEntity<>("Author is created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteCustomerById(@PathVariable(value = "id") long id) {
        customerUtil.deleteCustomer(id);
        return new ResponseEntity<>("Customer deleted successfully", HttpStatus.ACCEPTED);
    }
}
