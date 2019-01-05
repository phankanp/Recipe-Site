package phan.recipesite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import phan.recipesite.repository.RecipeRepository;
import phan.recipesite.repository.UserRepository;
import phan.recipesite.model.Category;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    RecipeRepository recipeDao;

    @Autowired
    UserRepository userDao;

    @Autowired
    UserService userService;

    @Autowired
    RecipeService recipeService;

    @Autowired
    IngredientService ingredientService;

    @Autowired
    StepService stepService;

    @Override
    public List<Recipe> findAll() {
        return recipeDao.findAll();
    }

    @Override
    public List<Category> getAllCategories() {
        return Arrays.asList(Category.values());
    }

    @Override
    public List<Recipe> findByCategory(Category category) {
        Category cat = Category.valueOf(category.toString().toUpperCase());

        if (cat == Category.ALL) {
            return findAll();
        } else {
            return recipeDao.findByCategory(cat);
        }
    }

    @Override
    public Recipe findById(Long id) {
        return recipeDao.findRecipeById(id);
    }

    @Override
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

        userDao.save(user);

        recipeDao.save(recipe);
    }

    @Override
    public void save(Recipe recipe) {
        recipeDao.save(recipe);
    }


    public Recipe savetest(Recipe recipe, User user) {
        Recipe _recipe = recipeDao.save(
                new Recipe(
                        recipe.getImage(),
                        recipe.getName(),
                        recipe.getDescription(),
                        recipe.getPrepTime(),
                        recipe.getCookTime(),
                        recipe.getCategory(),
                        recipe.getIngredients(),
                        recipe.getSteps()
                )
        );

        _recipe.setUser(user);

        recipeDao.save(_recipe);

        return _recipe;
    }


    @Override
    public void delete(Recipe recipe) {
        List<User> users = userDao.findByFavoritesId(recipe.getId());

        users.forEach(user -> userService.toggleFavorite(recipe.getUser(), recipe));

        users.forEach(userDao::save);

        recipeDao.save(recipe);
        recipeDao.delete(recipe);
    }

    @Override
    public List<Recipe> findByDescriptionOrIngredients(String search) {
        return recipeDao.findByDescriptionContainingOrIngredientsNameIgnoreCase(search, search);
    }

}
