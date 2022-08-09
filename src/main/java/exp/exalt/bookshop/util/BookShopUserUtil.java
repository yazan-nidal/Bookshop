package exp.exalt.bookshop.util;

import exp.exalt.bookshop.exceptions.GeneralException;
import exp.exalt.bookshop.models.BookShopUser;
import exp.exalt.bookshop.services.BookShopUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static exp.exalt.bookshop.constants.ConstVar.SERVER_ERROR;

@Service
public class BookShopUserUtil {
    @Autowired
    BookShopUserService bookShopUserService;

    public BookShopUser getUserByUsername(String username) {
        BookShopUser bookShopUser = null;
        try {
              bookShopUser = bookShopUserService.findUserByUsername(username);
        } catch (IllegalArgumentException ex) {
            throw new GeneralException(SERVER_ERROR);
        }
        return bookShopUser;
    }

    //method helper  user
    public int getUserRole(String username) {
        BookShopUser bookShopUser = null;
        try {
              bookShopUser = getUserByUsername(username);
              if(bookShopUser == null) {
                  throw new GeneralException(SERVER_ERROR);
              }
        } catch (NullPointerException ex) {
            throw new GeneralException(SERVER_ERROR);
        }
        return bookShopUser.getRole();
    }
}
