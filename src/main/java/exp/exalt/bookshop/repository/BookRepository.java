package exp.exalt.bookshop.repository;

import exp.exalt.bookshop.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book,Long> {
    public Optional<Book> findByIsbn(long isbn);
    public Optional<Book> findByName(String name);
    @Transactional
    public long deleteByIsbn(long isbn);
}
