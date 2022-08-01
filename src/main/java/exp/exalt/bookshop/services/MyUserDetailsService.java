package exp.exalt.bookshop.services;

import exp.exalt.bookshop.models.Author;
import exp.exalt.bookshop.models.BookShopUser;
import exp.exalt.bookshop.models.Customer;
import exp.exalt.bookshop.models.MyUserDetails;
import exp.exalt.bookshop.repositories.AuthorRepository;
import exp.exalt.bookshop.repositories.BookShopUserRepositories;
import exp.exalt.bookshop.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

// this just for test Jwt, will to implement full code on the soon

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    BookShopUserRepositories bookShopUserRepositories;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         BookShopUser bookShopUser = bookShopUserRepositories.findByUsername(username).orElse(null);
        if(bookShopUser  == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserDetails(bookShopUser);
    }
}
