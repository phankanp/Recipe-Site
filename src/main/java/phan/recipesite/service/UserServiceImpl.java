package phan.recipesite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import phan.recipesite.dao.RoleDao;
import phan.recipesite.dao.UserDao;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void toggleFavorite(User user, Recipe recipe) {
        if (user.getFavorites().contains(recipe)) {
            user.getFavorites().remove(recipe);
        } else {
            user.getFavorites().add(recipe);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with %s doesn't exist!", username));
        }
        return new UserAdapter(user);
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPasswordConfirm(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }
}
