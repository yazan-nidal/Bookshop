package exp.exalt.bookshop.services;

import exp.exalt.bookshop.models.Customer;
import exp.exalt.bookshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional

    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }
    @Transactional
    public Customer getCustomerById(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Transactional
    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public List<Customer> getCustomersByName(String name) {
        return customerRepository.findAllByName(name);
    }

    @Transactional
    public Customer addCustomerOrUpdate(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(long id) {
        customerRepository.deleteById(id);
    }

    @Transactional
    public void deleteCustomer(String username) {
        customerRepository.deleteByUsername(username);
    }
}
