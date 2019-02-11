package phan.recipesite.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
import phan.recipesite.service.*;
import phan.recipesite.util.CustomErrorType;
import phan.recipesite.web.FlashMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class RecipeController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UserService userService;
    private final RecipeService recipeServiceimpl;
    private final CommentService commmentService;

    @Autowired
    public RecipeController(IngredientService ingredientService, RecipeService recipeService, UserService
            userService, RecipeService recipeServiceimpl, CommentService commmentService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.userService = userService;
        this.recipeServiceimpl = recipeServiceimpl;
        this.commmentService = commmentService;
    }


    // Index - Home page of all recipes
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String recipesIndex(Model model) {

        List<Recipe> recipes = recipeService.findAll();
        List<Category> categories = recipeService.getAllCategories();

        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", Category.ALL);

        return "index";
    }

    // Favorite/Unfavorite existing recipes
    @PostMapping("/recipes/{id}/favorite")
    @ResponseBody
    public Map<String, Object> favoriteRecipe(@PathVariable("id") Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        Recipe recipe = recipeService.findById(id);
        userService.toggleFavorite(user, recipe);
        userService.save(user);

        boolean hasFavorite = recipe.getUserFavorites().contains(authentication.getName());

        Map<String, Object> resultJson = new HashMap<>();
        System.out.println(resultJson.toString());
        resultJson.put("favorited", hasFavorite);

        return resultJson;
    }

    // Sort recipes by category
    @RequestMapping(value = "/recipes/category/{category}", method = RequestMethod.GET)
    public String recipesByCategory(@PathVariable Category category, Model model) {
        List<Category> categories = recipeService.getAllCategories();

        List<Recipe> recipes = recipeService.findByCategory(category);

        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", Category.valueOf(category.toString().toUpperCase()));

        return "index";
    }

    // Details for a single recipe
    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
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
        model.addAttribute("redirect", "/");
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

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Successfully saved recipe!", FlashMessage
                .Status.SUCCESS));

        return "redirect:/details/" + recipe.getId();
    }

    // Edit recipe form with existing recipe details
    @RequestMapping(value = "/recipes/{id}/edit", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated() and hasPermission(#id, 'Recipe', 'ROLE_ADMIN')")
    public String editRecipeForm(@PathVariable Long id, Model model, RedirectAttributes
            redirectAttributes, Authentication authentication, HttpServletRequest request) {

        Recipe recipe = recipeService.findById(id);

        if (!model.containsAttribute("recipe")) {
            model.addAttribute("recipe", recipe);
        }

        model.addAttribute("recipe", recipe);
        model.addAttribute("categories", Arrays.asList(Category.values()));
        model.addAttribute("redirect", "/details/" + recipe.getId());
        model.addAttribute("heading", "Edit Recipe");
        model.addAttribute("action", "/recipes/" + id);
        model.addAttribute("submit", "Save");

        return "recipeForm";
    }

    // Edit an existing recipe
    @RequestMapping(value = "/recipes/{id}", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated() and hasPermission(#recipe, 'ROLE_USER')")
    public String editRecipe(@Valid Recipe recipe, BindingResult result, @PathVariable Long id, @RequestParam
            MultipartFile imageFile,
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

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Successfully saved recipe!", FlashMessage
                .Status.SUCCESS));

        return "redirect:/details/" + recipe.getId();
    }

    // Delete a recipe
    @RequestMapping(value = "/recipes/{id}/delete", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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
            return new ResponseEntity<>(new CustomErrorType("You can only delete recipes which you created"),
                    HttpStatus.FORBIDDEN);
        }
    }

    // Search for recipes by description or ingredients
    @RequestMapping(value = "/recipes/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "searchq", required = false) String searchq, Model model, Authentication
            authentication) {

        List<Category> categories = recipeService.getAllCategories();

        List<Recipe> recipes = new ArrayList<>();
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

    @RequestMapping(value = "/recipe/comments", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addComment(@RequestBody Comment comment, @RequestParam(value = "recipe_id") long recipeID, BindingResult result, RedirectAttributes redirectAttributes, Authentication
            authentication){

       Recipe recipe = recipeService.findById(recipeID);

        String commentCreator = authentication.getName();

        Comment newComment = (commmentService.save(comment, recipe, commentCreator));

        Map<String, String> map = new HashMap<>();
        map.put("commentBody", newComment.getBody());
        map.put("createdBy", newComment.getCreatedBy());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
