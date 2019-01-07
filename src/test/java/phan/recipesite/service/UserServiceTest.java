package phan.recipesite.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;
import phan.recipesite.repository.UserRepository;
import phan.recipesite.web.controller.RecipeController;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static phan.recipesite.data.TestData.recipesList;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userDao;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @InjectMocks
    private RecipeController recipeController;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

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
        Recipe recipe = recipesList.get(0);

        User user = recipe.getUser();

        userService.toggleFavorite(user, recipe);

        assertEquals(user.getFavorites().size(), 1);
    }

    @Test
    public void toggleFavorite_ShouldRemoveFavorite() throws Exception {
        Recipe recipe = recipesList.get(0);

        User user = recipe.getUser();

        user.getFavorites().add(recipe);

        userService.toggleFavorite(user, recipe);

        assertEquals(user.getFavorites().size(), 0);
    }
}
