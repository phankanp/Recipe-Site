package phan.recipesite.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import phan.recipesite.model.User;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    List<User> findByFavoritesId(Long id);

    User findByUsername(String username);
}
