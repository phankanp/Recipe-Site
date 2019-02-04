package phan.recipesite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phan.recipesite.model.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFavoritesId(Long id);

    User findByUsername(String username);

    User findByEmail(String email);
}
