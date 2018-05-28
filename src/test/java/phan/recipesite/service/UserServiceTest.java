package phan.recipesite.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import phan.recipesite.dao.UserDao;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    public void findByUsername() throws Exception {
        User user = new User();
        user.setUsername("user1");

        when(userDao.findByUsername("user1")).thenReturn(user);

        assertEquals("findByUsername returns matching username",
                user.getUsername(),
                userService.findByUsername("user1").getUsername());
        verify(userDao).findByUsername("user1");
    }

    @Test
    public void toggleFavorite_ShouldAddFavorite() throws Exception {
        User user = new User();
        Recipe recipe = new Recipe();

        userService.toggleFavorite(user, recipe);

        assertEquals(user.getFavorites().size(), 1);
    }

    @Test
    public void toggleFavorite_ShouldRemoveFavorite() throws Exception {
        User user = new User();
        Recipe recipe = new Recipe();
        user.getFavorites().add(recipe);

        userService.toggleFavorite(user, recipe);

        assertEquals(user.getFavorites().size(), 0);
    }
}
