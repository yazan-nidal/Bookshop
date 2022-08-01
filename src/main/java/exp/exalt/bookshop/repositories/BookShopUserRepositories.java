package exp.exalt.bookshop.repositories;

import exp.exalt.bookshop.models.BookShopUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookShopUserRepositories extends CrudRepository<BookShopUser,String> {
    public Optional<BookShopUser> findByUsername(String username);
}
