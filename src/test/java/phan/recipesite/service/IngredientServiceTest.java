package phan.recipesite.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import phan.recipesite.dao.IngredientDao;
import phan.recipesite.model.Ingredient;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IngredientServiceTest {
    @Mock
    IngredientDao ingredientDao;

    @InjectMocks
    private IngredientService ingredientService = new IngredientServiceImpl();

    private List<Ingredient> ingredients;

    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(ingredientDao.findIngredientById(1L)).thenReturn(new Ingredient());

        assertThat(ingredientService.findById(1L), instanceOf(Ingredient.class));

        verify(ingredientDao).findIngredientById(1L);
    }

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        List<Ingredient> ingredients = Arrays.asList(new Ingredient(), new Ingredient());

        when(ingredientDao.findAll()).thenReturn(ingredients);

        assertEquals("findAll should return two ingredients", 2, ingredientService.findAll().size());

        verify(ingredientDao).findAll();
    }
}
