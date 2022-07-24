package exp.exalt.bookshop.service;

import exp.exalt.bookshop.exception.EmptyEntityException;
import exp.exalt.bookshop.model.Customer;
import exp.exalt.bookshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        if(customers.isEmpty()) {
            throw new EmptyEntityException("don't have any Customer");
        }
        customers.forEach(customer -> customer.setName(customer.getName().toUpperCase().replaceAll("_+"," ")));
        return customers;
    }

    public Customer getCustomerById(long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            throw new EntityNotFoundException("Customer not Found");
        }
        customer.setName(customer.getName().toUpperCase().replaceAll("_+"," "));
        return customer;
    }

    public Customer getCustomerByName(String  name) {
        name = name.toLowerCase().replaceAll(" +","_");
        Customer customer = customerRepository.findByName(name).orElse(null);
        if(customer == null) {
            throw new EntityNotFoundException("Customer not Found");
        }
        customer.setName(customer.getName().toUpperCase().replaceAll("_+"," "));
        return customer;
    }

    public Customer addCustomer(Customer customer) {
        if(customerRepository.findById(customer.getId()).orElse(null) != null) {
            throw new EntityExistsException("Customer 'ID' is used please use another");
        }
        customer.setName(customer.getName().toLowerCase().replaceAll(" +","_"));
        if(customerRepository.findByName(customer.getName()).orElse(null) != null) {
            throw new EntityExistsException("Customer 'Name' is used please use another");
        }
        customerRepository.save(customer);
        return customer;
    }

//    public void updateCustomer(Customer customer, long id){
//        Customer customerT = this.getCustomer(id);
//        if(customerT != null) {
//            customerRepository.save(customer);
//        }
//    }
//
//    public void deleteCustomer(long id) {
//        if(this.getCustomer(id) != null) {
//            customerRepository.deleteById(id);
//        }
//    }
//
//    public List<Book> getBooks(long id) {
//        return this.getCustomer(id).getBooks();
//    }
//
//    public Book getBook(long id, long isbn){
//        return this.getCustomer(id).getBooks().stream()
//                .filter(b -> b.getIsbn() == isbn).findFirst().orElse(null);
//    }
//
//    public Author getBookAuthor(long id, long isbn){
//        return this.getCustomer(id).getBooks().stream()
//                .filter(b -> b.getIsbn() == isbn).findFirst().orElse(null).getAuthor();
//    }
//
//    public void rentBook(long id, Book book) {
//        Customer customer = customerRepository.findById(id).orElse(null);
//        if(customer != null) {
//            customer.getBooks().add(book);
//            customerRepository.save(customer);
//        }
//    }
//
//    public void returnBook(long id, long isbn) {
//        Customer customer = customerRepository.findById(id).orElse(null);
//        if(customer != null) {
//            customer.getBooks().removeIf(b -> b.getIsbn() == isbn);
//            customerRepository.save(customer);
//        }
//    }
}
