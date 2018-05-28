package phan.recipesite.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import phan.recipesite.model.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
