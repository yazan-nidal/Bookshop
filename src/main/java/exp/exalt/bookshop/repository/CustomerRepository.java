package exp.exalt.bookshop.repository;

import exp.exalt.bookshop.model.Author;
import exp.exalt.bookshop.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {
//    @Transactional
//    public void deleteBookById(long id);

    Optional<Customer> findByName(String name);
}
