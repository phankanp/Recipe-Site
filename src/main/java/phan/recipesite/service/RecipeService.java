package phan.recipesite.service;

import org.springframework.web.multipart.MultipartFile;
import phan.recipesite.model.Category;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;

import java.util.List;


public interface RecipeService {
    Recipe findById(Long id);

    List<Recipe> findAll();

    List<Recipe> findByCategory(Category category);

    List<Category> getAllCategories();

    List<Recipe> findByDescriptionOrIngredients(String search);

    void save(Recipe recipe, User user, MultipartFile image);

    void save(Recipe recipe);

    void delete(Recipe recipe);

}
