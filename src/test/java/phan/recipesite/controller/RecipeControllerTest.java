package phan.recipesite.controller;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import phan.recipesite.model.Category;
import phan.recipesite.model.Recipe;
import phan.recipesite.web.FlashMessage;
import phan.recipesite.web.controller.RecipeController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static phan.recipesite.data.TestData.recipesList;
import static phan.recipesite.data.TestData.user1;


@RunWith(MockitoJUnitRunner.class)
public class RecipeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private RecipeService recipeService;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private StepService stepService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecipeController recipeController;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void index_ShouldIncludeRecipesInModel() throws Exception {
        List<Recipe> recipes = recipesList;
        when(recipeService.findAll()).thenReturn(recipes);

        mockMvc.perform(MockMvcRequestBuilders.get("/").with(user("user1")))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("recipes", recipes));
        verify(recipeService).findAll();
    }

    @Test
    public void recipesByCategory_ShouldReturnRecipesWithGivenCategory() throws Exception {
        List<Recipe> recipes = Arrays.asList(recipesList.get(0));
        when(recipeService.findByCategory(Category.BREAKFAST)).thenReturn(recipes);

        mockMvc.perform(get("/recipes/category/BREAKFAST"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("selectedCategory", Category.BREAKFAST))
                .andExpect(model().attribute("recipes", recipes));
        verify(recipeService).findByCategory(Category.BREAKFAST);
    }

    @Test
    public void search_ShouldReturnRecipesWithGivenSearchTerm() throws Exception {
        List<Recipe> results = Arrays.asList(recipesList.get(0));
        when(recipeService.findByDescriptionOrIngredients("eggs")).thenReturn(results);

        mockMvc.perform(get("/recipes/search?searchq=eggs"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("recipes", results));
        verify(recipeService).findByDescriptionOrIngredients("eggs");
    }

    @Test
    public void recipeDetails_ShouldReturnRecipeDetailsPage() throws Exception {
        Recipe recipe = recipesList.get(0);
        recipe.setId(1L);

        when(recipeService.findById(1L)).thenReturn(recipe);


        mockMvc.perform(get("/details/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(model().attribute("recipe", recipe));
        verify(recipeService).findById(1L);
    }

    @Test
    public void recipeImage_ShouldReturnImageBytes() throws Exception {
        Recipe recipe = recipesList.get(0);
        recipe.setId(1L);

        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(get("/details/1"))
                .andReturn().getResponse().getContentAsByteArray().equals(recipe.getImage());
        verify(recipeService).findById(1L);
    }

    @Test
    public void formNewRecipe_ShouldReturnEditForm() throws Exception {
        mockMvc.perform(get("/recipes/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipeForm"));
    }

    @Test
    public void editRecipe_ShouldIncludeRecipeInModel() throws Exception {
        Recipe recipe = recipesList.get(0);
        recipe.setId(1L);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(get("/recipes/1/edit").with(user("user1")))
                .andExpect(status().isOk())
                .andExpect(view().name("recipeForm"))
                .andExpect(model().attribute("recipe", recipe));
        verify(recipeService).findById(1L);
    }

    @Test
    public void favorite_ShouldRedirectToReferer() throws Exception {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>(
                Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password", list);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Recipe recipe = recipesList.get(0);
        recipe.setId(1L);
        when(userService.findByUsername("user1")).thenReturn(user1());
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(post("/recipes/1/favorite").with(user("user1"))
                .header("referer", "/")
                .principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(userService).toggleFavorite(user1(), recipe);
    }

    @Test
    public void deleteRecipe_ShouldRedirectToIndex() throws Exception {
        Recipe recipe = recipesList.get(0);
        recipe.setId(1L);

        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(post("/recipes/1/delete"))
                .andExpect(flash().attribute("flash", hasProperty("status", is(FlashMessage.Status.SUCCESS))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(recipeService).delete(recipe);
    }
}
