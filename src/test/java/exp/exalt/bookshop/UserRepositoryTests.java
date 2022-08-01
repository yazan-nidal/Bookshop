package exp.exalt.bookshop;

import static org.assertj.core.api.Assertions.assertThat;

import exp.exalt.bookshop.models.BookShopUser;
import exp.exalt.bookshop.models.Role;
import exp.exalt.bookshop.repositories.BookShopUserRepositories;
import exp.exalt.bookshop.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookShopUserRepositories userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Test
    void testCreateUser() {
        BookShopUser user = new BookShopUser();
        user.setUsername("testI");
        user.setPassword("exalt_00");
        user.setId((long)5);
        BookShopUser savedUser = userRepo.save(user);
        BookShopUser existUser = entityManager.find(BookShopUser.class, savedUser.getId());
        assertThat(user.getUsername()).isEqualTo(existUser.getUsername());
    }

    @Test
    void testAddRoleToNewUser() {
        Role roleAdmin = roleRepo.findByName("AUTHOR");
        BookShopUser user = new BookShopUser();
        user.setUsername("testI");
        user.setPassword("exalt_00");
        user.setId((long)5);
        user.getRoles().add(roleAdmin);
        BookShopUser savedUser = userRepo.save(user);
        assertThat(""+savedUser.getRoles().size()).hasSize(1);
    }

    @Test
    void testAddRoleToExistingUser() {
        BookShopUser user = userRepo.findByUsername("testI").orElse(null);
        Role roleUser = roleRepo.findByName("ADMIN");
        Role roleCustomer = new Role(4,"TEST");
        user.getRoles().add(roleUser);
        user.getRoles().add(roleCustomer);
        BookShopUser savedUser = userRepo.save(user);
        assertThat(""+savedUser.getRoles().size()).hasSize(2);
    }
}
