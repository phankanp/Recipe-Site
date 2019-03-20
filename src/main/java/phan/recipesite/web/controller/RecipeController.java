package phan.recipesite.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import phan.recipesite.model.*;
import phan.recipesite.service.CommentService;
import phan.recipesite.service.RecipeService;
import phan.recipesite.service.UserService;
import phan.recipesite.web.FlashMessage;

import javax.validation.Valid;
import java.util.*;

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final RecipeService recipeServiceimpl;
    private final CommentService commmentService;

    public RecipeController(RecipeService recipeService, UserService
            userService, RecipeService recipeServiceimpl, CommentService commmentService) {

        this.recipeService = recipeService;
        this.userService = userService;
        this.recipeServiceimpl = recipeServiceimpl;
        this.commmentService = commmentService;
    }

    @RequestMapping("/")
    public String redirectPage() {
        return "redirect:recipes";
    }


    // Index - Home page of all recipes
    @RequestMapping(value = "/recipes", method = RequestMethod.GET)
    public String recipesIndex(Model model) {

        List<Recipe> recipes = recipeService.findAll();
        List<Category> categories = recipeService.getAllCategories();

        List<Recipe> breakfast = recipeService.findByCategory(Category.BREAKFAST);
        List<Recipe> lunch = recipeService.findByCategory(Category.LUNCH);
        List<Recipe> dinner = recipeService.findByCategory(Category.DINNER);
        List<Recipe> dessert = recipeService.findByCategory(Category.DESSERT);

        model.addAttribute("breakfast", breakfast);
        model.addAttribute("lunch", lunch);
        model.addAttribute("dinner", dinner);
        model.addAttribute("dessert", dessert);

        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", Category.ALL);

        return "index";
    }

    // Favorite/Unfavorite existing recipes
    @RequestMapping(value = "/recipes/{id}/favorite", method = RequestMethod.POST)
    public ResponseEntity<?> favoriteRecipe(@PathVariable("id") Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .header("error", "/login")
                    .body("Custom login header set");
        }

        Recipe recipe = recipeService.findById(id);
        userService.toggleFavorite(user, recipe);

        boolean hasFavorite = recipe.getUserFavorites().contains(authentication.getName());

        Map<String, Object> resultJson = new HashMap<>();
        System.out.println(resultJson.toString());
        resultJson.put("favorited", hasFavorite);

        return new ResponseEntity<>(resultJson, HttpStatus.OK);
    }

    // Sort recipes by category
    @RequestMapping(value = "/recipes/category/{category}", method = RequestMethod.GET)
    public String recipesByCategory(@PathVariable Category category, Model model) {
        List<Category> categories = recipeService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", Category.valueOf(category.toString().toUpperCase()));

        return "index";
    }

    // Details for a single recipe
    @RequestMapping(value = "/recipes/{id}", method = RequestMethod.GET)
    public String recipeDetails(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.findById(id);

        Comment comment = new Comment();
        comment.setRecipe(recipe);

        model.addAttribute("recipe", recipe);
        model.addAttribute("comment", comment);

        return "detail";
    }

    // Gets recipe image
    @GetMapping(value = "/recipes/image/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] recipeImage(@PathVariable Long imageId) {

        return recipeService.findById(imageId).getImage();
    }

    // Add recipe form
    @RequestMapping(value = "/recipes/add", method = RequestMethod.GET)
    public String formNewRecipe(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        List<Step> steps = new ArrayList<>();
        List<Category> categories = recipeService.getAllCategories();

        if (!model.containsAttribute("recipe")) {
            Recipe recipe = new Recipe();
            recipe.getSteps().add(new Step());
            recipe.getIngredients().add(new Ingredient());
            model.addAttribute("recipe", recipe);
        }


        model.addAttribute("action", "/recipes");
        model.addAttribute("heading", "New Recipe");
        model.addAttribute("redirect", "/recipes");
        model.addAttribute("categories", categories);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("instructions", steps);

        return "recipeForm";
    }

    // Add new recipe
    @RequestMapping(value = "/recipes", method = RequestMethod.POST)
    public String addRecipe(@Valid Recipe recipe, BindingResult result, @RequestParam MultipartFile imageFile,
                            RedirectAttributes redirectAttributes, Authentication authentication) {

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            List<String> messages = new ArrayList<>();

            for (FieldError e : errors) {
                messages.add(e.getDefaultMessage());
            }

            redirectAttributes.addFlashAttribute("flash", messages);
            redirectAttributes.addFlashAttribute("recipe", recipe);
            return "redirect:/recipes/add";
        }

        User user = userService.findByUsername(authentication.getName());

        recipeService.save(recipe, user, imageFile);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Successfully saved recipe!"));

        return "redirect:/recipes/" + recipe.getId();
    }

    // Edit recipe form with existing recipe details
    @RequestMapping(value = "/recipes/{id}/edit", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'Recipe', 'ROLE_ADMIN')")
    public String editRecipeForm(@PathVariable Long id, Model model) {

        Recipe recipe = recipeService.findById(id);

        if (!model.containsAttribute("recipe")) {
            model.addAttribute("recipe", recipe);
        }

        model.addAttribute("recipe", recipe);
        model.addAttribute("categories", Arrays.asList(Category.values()));
        model.addAttribute("redirect", "/recipes");
        model.addAttribute("heading", "Edit Recipe");
        model.addAttribute("action", "/recipes/" + id);
        model.addAttribute("submit", "Save");

        return "recipeForm";
    }

    // Edit an existing recipe
    @RequestMapping(value = "/recipes/{id}", method = RequestMethod.POST)
    public String editRecipe(@Valid Recipe recipe, BindingResult result, @RequestParam MultipartFile imageFile,
                             RedirectAttributes redirectAttributes, Authentication authentication) {

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            List<String> messages = new ArrayList<>();

            for (FieldError e : errors) {
                messages.add(e.getDefaultMessage());
            }

            redirectAttributes.addFlashAttribute("flash", messages);
            redirectAttributes.addFlashAttribute("recipe", recipe);
            return "redirect:/recipes/" + recipe.getId() + "/edit";
        }

        User user = userService.findByUsername(authentication.getName());

        recipeServiceimpl.save(recipe, user, imageFile);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Successfully saved recipe!"));

        return "redirect:/recipes/" + recipe.getId();
    }

    // Delete a recipe
    @RequestMapping(value = "/recipes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") Long id, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        User recipeUser = recipeService.findById(id).getUser();

        List<String> roles = new ArrayList<>();

        for (Role role : currentUser.getRoles()) {
            roles.add(role.getName());
        }

        if (currentUser == recipeUser || roles.contains("ROLE_ADMIN")) {
            recipeService.delete(recipeService.findById(id));
            return new ResponseEntity<>("Recipe has been deleted!", HttpStatus.OK);
        } else {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .header("error", "/403")
                    .body("Custom 403 header set");
        }
    }

    // Search for recipes by description or ingredients
    @RequestMapping(value = "/recipes/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "searchq", required = false) String searchq, Model model) {

        List<Category> categories = recipeService.getAllCategories();

        List<Recipe> recipes;
        List<Recipe> results = new ArrayList<>();

        if (searchq != null) {
            recipes = recipeService.findByDescriptionOrIngredients(searchq);

            for (Recipe r : recipes) {
                if (!results.contains(r)) {
                    results.add(r);
                }
            }
        }

        model.addAttribute("categories", categories);
        model.addAttribute("recipes", results);
        model.addAttribute("selectedCategory", Category.ALL);

        return "index";
    }

    // Add comments to posts
    @RequestMapping(value = "/recipe/comments", method = RequestMethod.POST)
    public ResponseEntity<?> addComment(@RequestBody Comment comment, @RequestParam(value = "recipe_id") long recipeID,
                                        Authentication authentication) {

        Recipe recipe = recipeService.findById(recipeID);

        String commentCreator = authentication.getName();

        if (commentCreator == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .header("error", "/login")
                    .body("Custom comment header set");
        }

        Comment newComment = (commmentService.save(comment, recipe, commentCreator));

        Map<String, String> map = new HashMap<>();
        map.put("commentBody", newComment.getBody());
        map.put("createdBy", newComment.getCreatedBy());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
