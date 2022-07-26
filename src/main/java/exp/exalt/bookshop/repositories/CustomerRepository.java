package exp.exalt.bookshop.repositories;

import exp.exalt.bookshop.models.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {
    List<Customer> findAllByName(String name);
}
