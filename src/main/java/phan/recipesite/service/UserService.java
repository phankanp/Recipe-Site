package phan.recipesite.service;

import jdk.nashorn.internal.ir.IfNode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;
import phan.recipesite.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder
            bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findByFavoritesId(Long id) {
        return userRepository.findByFavoritesId(id);
    }

    public void toggleFavorite(User user, Recipe recipe) {
        if (user.getFavorites().contains(recipe)) {
            user.getFavorites().remove(recipe);
        } else {
            user.getFavorites().add(recipe);
        }
        userRepository.save(user);
    }

    public void upVoteDownVoteTracker(User user, Long recipeId, short direction) {

        System.out.println(recipeId + "*****************");
        if (direction == 1) {
            if (user.getDownvotes().contains(recipeId)) {
                user.getDownvotes().remove(recipeId);
            }
            user.getUpvotes().add(recipeId);
        } else if (direction == -1) {
            if (user.getUpvotes().contains(recipeId)) {
                user.getUpvotes().remove(recipeId);
            }
            user.getDownvotes().add(recipeId);
        }

        userRepository.save(user);
    }

    public void save(User user) {
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setPasswordConfirm(password);

        user.addRole(roleService.findByName("ROLE_USER"));

        userRepository.save(user);
    }
}
