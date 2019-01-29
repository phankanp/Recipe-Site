package phan.recipesite.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import phan.recipesite.model.Category;
import phan.recipesite.model.Recipe;
import phan.recipesite.repository.RecipeRepository;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static phan.recipesite.data.TestData.recipesList;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeDao;

    @InjectMocks
    private RecipeService recipeService = new RecipeService();

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        List<Recipe> recipes = Arrays.asList(new Recipe(), new Recipe());

        when(recipeDao.findAll()).thenReturn(recipes);

        assertEquals("findAll should return two recipes", 2, recipeService.findAll().size());
        verify(recipeDao).findAll();
    }

    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(recipeDao.findRecipeById(1L)).thenReturn(new Recipe());

        assertThat(recipeService.findById(1L), instanceOf(Recipe.class));
        verify(recipeDao).findRecipeById(1L);
    }

    @Test
    public void findByCategory_ReturnsOneRecipe() throws Exception {
        when(recipeDao.findByCategory(Category.DESSERT)).thenReturn(Arrays.asList(recipesList.get(1)));

        List<Recipe> recipes = recipeService.findByCategory(Category.DESSERT);

        assertThat(recipes.size(), is(equalTo(1)));
        verify(recipeDao).findByCategory(Category.DESSERT);

    }

    @Test
    public void getAllCategories() throws Exception {
        List<Category> categories = recipeService.getAllCategories();

        assertThat(categories.size(), is(equalTo(5)));
    }

    @Test
    public void findByDescriptionOrIngredients_ShouldReturnRecipesWithGivenDescriptionOrIngredient() throws Exception {
        when(recipeDao.findByDescriptionContainingOrIngredientsNameIgnoreCase("milk", "milk")).thenReturn(Arrays
                .asList(recipesList.get(0), recipesList.get(1)));

        List<Recipe> recipes = recipeDao.findByDescriptionContainingOrIngredientsNameIgnoreCase("milk", "milk");

        assertThat(recipes.size(), is(equalTo(2)));
        verify(recipeDao).findByDescriptionContainingOrIngredientsNameIgnoreCase("milk", "milk");
    }

}
