package exp.exalt.bookshop.repositories;

import exp.exalt.bookshop.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role,Integer> {
    public List<Role> findAll();
    public Role findByName(String name);
}
