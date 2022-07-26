package exp.exalt.bookshop.repositories;

import exp.exalt.bookshop.models.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book,Long> {
    public Optional<Book> findByIsbn(long isbn);
    public List<Book> findAllByName(String name);
    @Transactional
    public long deleteByIsbn(long isbn);
}
