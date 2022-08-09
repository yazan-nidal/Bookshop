package exp.exalt.bookshop.services;

import exp.exalt.bookshop.models.BookShopUser;
import exp.exalt.bookshop.repositories.BookShopUserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookShopUserService {
    @Autowired
    BookShopUserRepositories bookShopUserRepositories;

    public BookShopUser findUserByUsername(String username) {
        return bookShopUserRepositories
                .findByUsername(username).orElse(null);
    }
}
