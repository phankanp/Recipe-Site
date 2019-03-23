package phan.recipesite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phan.recipesite.model.Category;
import phan.recipesite.model.Ingredient;
import phan.recipesite.model.Recipe;

import java.util.List;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAll();

    Recipe findRecipeById(Long id);

    List<Recipe> findByCategory(Category category);

    List<Recipe> findByDescriptionContainingIgnoreCase(String search);

    List<Recipe> findByIngredientsContainsIgnoreCase(Ingredient search);

    Recipe findRecipeByComments(Long id);
}
