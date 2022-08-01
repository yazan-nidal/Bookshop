package exp.exalt.bookshop;

import java.util.List;

import exp.exalt.bookshop.models.Role;
import exp.exalt.bookshop.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class RoleRepositoryTests {

    @Autowired private RoleRepository repo;

    @Test
    void testCreateRoles() {
        Role author = new Role(2,"AUTHOR");
        Role admin = new Role(1,"ADMIN");
        Role customer = new Role(3,"CUSTOMER");

        repo.saveAll(List.of(author, admin, customer));

        List<Role> listRoles = repo.findAll();

        assertThat(listRoles.size()+"").hasSize(3);
    }

}
