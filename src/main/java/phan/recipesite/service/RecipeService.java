package phan.recipesite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import phan.recipesite.model.Category;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;
import phan.recipesite.repository.RecipeRepository;
import phan.recipesite.repository.UserRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    RecipeService recipeService;

    @Autowired
    IngredientService ingredientService;

    @Autowired
    StepService stepService;


    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }


    public List<Category> getAllCategories() {
        return Arrays.asList(Category.values());
    }


    public List<Recipe> findByCategory(Category category) {
        Category cat = Category.valueOf(category.toString().toUpperCase());

        if (cat == Category.ALL) {
            return findAll();
        } else {
            return recipeRepository.findByCategory(cat);
        }
    }


    public Recipe findById(Long id) {
        return recipeRepository.findRecipeById(id);
    }


    public void save(Recipe recipe, User user, MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                byte[] image = imageFile.getBytes();
                recipe.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (recipe.getId() != null) {
            byte[] image = recipeService.findById(recipe.getId()).getImage();
            recipe.setImage(image);
        }

        recipe.getIngredients().forEach(ingredient -> ingredientService.save(ingredient));
        recipe.getSteps().forEach(step -> stepService.save(step));
        recipe.setUser(user);

        userService.save(user);

        recipeRepository.save(recipe);
    }


    public void save(Recipe recipe) {
        recipeRepository.save(recipe);
    }


    public void delete(Recipe recipe) {
        List<User> users = userService.findByFavoritesId(recipe.getId());

        users.forEach(user -> userService.toggleFavorite(recipe.getUser(), recipe));

        users.forEach(userService::save);

        recipeRepository.save(recipe);
        recipeRepository.delete(recipe);
    }

    public List<Recipe> findByDescriptionOrIngredients(String search) {
        return recipeRepository.findByDescriptionContainingOrIngredientsNameIgnoreCase(search, search);
    }
}
