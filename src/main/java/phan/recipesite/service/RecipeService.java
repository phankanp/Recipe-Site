package phan.recipesite.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import phan.recipesite.model.*;
import phan.recipesite.repository.RecipeRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final UserService userService;

    private final IngredientService ingredientService;

    private final StepService stepService;

    private final CommentService commentService;

    public RecipeService(RecipeRepository recipeRepository, UserService userService, IngredientService
            ingredientService, StepService stepService, CommentService commentService) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
        this.ingredientService = ingredientService;
        this.stepService = stepService;
        this.commentService = commentService;
    }

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
            byte[] image = recipeRepository.findRecipeById(recipe.getId()).getImage();
            recipe.setImage(image);
        }

        recipe.setUser(user);
        recipeRepository.save(recipe);

        recipe.getIngredients().forEach(ingredient ->
                ingredient.setRecipe(recipe));

        recipe.getIngredients().forEach(ingredient ->
                ingredientService.save(ingredient));

        recipe.getSteps().forEach(step ->
                step.setRecipe(recipe));

        recipe.getSteps().forEach(step ->
                stepService.save(step));
    }

    public void save(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public void delete(Recipe recipe) {
        List<User> users = userService.findByFavoritesId(recipe.getId());
        List<Comment> comments = recipe.getComments();
        List<Ingredient> ingredients = recipe.getIngredients();
        List<Step> steps = recipe.getSteps();

        comments.forEach(comment -> commentService.delete(comment));
        ingredients.forEach(ingredient -> ingredientService.delete(ingredient));
        steps.forEach(step -> stepService.delete(step));

        users.forEach(user -> userService.toggleFavorite(recipe.getUser(), recipe));

        users.forEach(userService::save);

        recipeRepository.delete(recipe);
    }

    public List<Recipe> findByDescriptionOrIngredients(String search) {

        List<Recipe> results = new ArrayList<>(recipeRepository.findByDescriptionContainingIgnoreCase(search));

//        results.addAll(recipeRepository.findByIngredientsContainsIgnoreCase(ingredient));

        List<Recipe> allRecipes = recipeRepository.findAll();

        for (Recipe r : allRecipes) {
            for (Ingredient i : r.getIngredients()) {
                System.out.println(i.toString()+"******************************");
                if (i.toString().contains(search)) {
                    results.add(r);
                    break;
                }
            }
        }

        return results;
    }
}
