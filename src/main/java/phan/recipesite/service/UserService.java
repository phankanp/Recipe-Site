package phan.recipesite.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);

    void toggleFavorite(User user, Recipe recipe);

    void save(User user);

}
