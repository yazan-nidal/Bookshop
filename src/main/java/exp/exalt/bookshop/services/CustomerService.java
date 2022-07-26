package exp.exalt.bookshop.services;

import exp.exalt.bookshop.models.Customer;
import exp.exalt.bookshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        return customers;
    }

    public Customer getCustomerById(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public List<Customer> getCustomersByName(String name) {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAllByName(name).forEach(customers::add);
        return customers;
    }

    public Customer addCustomer(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    public void deleteCustomer(long id) {
        customerRepository.deleteById(id);
    }
}
