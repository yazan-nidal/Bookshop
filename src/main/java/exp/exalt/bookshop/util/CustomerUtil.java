package exp.exalt.bookshop.util;

import exp.exalt.bookshop.exceptions.customer_exceptions.CustomerExistsException;
import exp.exalt.bookshop.exceptions.customer_exceptions.CustomerNotFoundException;
import exp.exalt.bookshop.exceptions.customer_exceptions.EmptyCustomerListException;
import exp.exalt.bookshop.models.Customer;
import exp.exalt.bookshop.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static exp.exalt.bookshop.constants.ConstVar.CUSTOMER_CONFLICT;
import static exp.exalt.bookshop.constants.ConstVar.CUSTOMER_NOT_FOUND;

@Service
public class CustomerUtil {
    @Autowired
    CustomerService customerService;

    public List<Customer> getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        if(customers.isEmpty()) {
            throw new EmptyCustomerListException();
        }
        return customers;
    }

    public Customer getCustomerById(long id) {
        Customer customer = customerService.getCustomerById(id);
        if(customer == null){
            throw new CustomerNotFoundException(CUSTOMER_NOT_FOUND);
        }
        return customer;
    }

    public List<Customer> getCustomersByName(String name) {
        List<Customer> customers =  customerService.getCustomersByName(name);
        if( customers.isEmpty()) {
            throw new CustomerNotFoundException(CUSTOMER_NOT_FOUND);
        }
        return  customers;
    }

    public Customer addCustomer(Customer customer) {
        if( customerService.getCustomerById( customer.getId()) != null) {
            throw new CustomerExistsException(CUSTOMER_CONFLICT);
        }
        customerService.addCustomer( customer);
        return  customer;
    }

    public void deleteCustomer(long id) {
        Customer  customer =  customerService.getCustomerById(id);
        if(customer == null){
            throw new CustomerNotFoundException(CUSTOMER_NOT_FOUND);
        }
        customerService.deleteCustomer(id);
    }
}
