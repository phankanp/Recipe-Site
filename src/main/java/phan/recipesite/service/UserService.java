package phan.recipesite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;
import phan.recipesite.repository.RoleRepository;
import phan.recipesite.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findByFavoritesId(Long id) {return userRepository.findByFavoritesId(id);}

    public void toggleFavorite(User user, Recipe recipe) {
        if (user.getFavorites().contains(recipe)) {
            user.getFavorites().remove(recipe);
        } else {
            user.getFavorites().add(recipe);
        }
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with %s doesn't exist!", username));
        }
        return new UserAdapter(user);
    }

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPasswordConfirm(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
