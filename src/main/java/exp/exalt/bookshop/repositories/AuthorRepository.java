package exp.exalt.bookshop.repositories;

import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.models.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author,Long> {
    List<Author> findAllByName(String name);
}