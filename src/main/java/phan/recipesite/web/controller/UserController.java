package phan.recipesite.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import phan.recipesite.model.Category;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;
import phan.recipesite.model.validator.PasswordValidator;
import phan.recipesite.service.RecipeService;
import phan.recipesite.service.UserService;
import phan.recipesite.web.FlashMessage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    private final RecipeService recipeService;

    private final PasswordValidator passwordValidator;

    public UserController(UserService userService, RecipeService recipeService, PasswordValidator passwordValidator) {
        this.userService = userService;
        this.recipeService = recipeService;
        this.passwordValidator = passwordValidator;
    }

    // User sign up form
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }

        return "/signup";
    }

    // Add new user
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        passwordValidator.validate(user, result);

        if (result.hasErrors()) {
            return "signup";
        }

        userService.save(user);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Successfully created account! Please login."));

        return "redirect:/recipes";
    }

    // Login form
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, String error, String logout) {
        model.addAttribute("user", new User());

        if (error != null) {
            model.addAttribute("error", "Your username or password is invalid.");
        }

        if (logout != null)
            model.addAttribute("logout", "You have been logged out successfully.");

        return "login";
    }

//    // User profile
//    @RequestMapping(value = "/userprofile", method = RequestMethod.GET)
//    public String userProfile(Model model, Authentication authentication) {
//
//        User user = userService.findByUsername(authentication.getName());
//
//        model.addAttribute("user", user);
//
//        return "profile";
//    }

    private void userUpDownVotes(Model model, Authentication authentication) {
        getUpDownVotes(model, authentication, userService);
    }

    static void getUpDownVotes(Model model, Authentication authentication, UserService userService) {
        try {
            if ( userService.findByUsername(authentication.getName()) != null) {
                List<Long> upvotes = userService.findByUsername(authentication.getName()).getUpvotes();
                List<Long> downvotes = userService.findByUsername(authentication.getName()).getDownvotes();

                model.addAttribute("upvotes", upvotes);
                model.addAttribute("downvotes", downvotes);

            }
        } catch (NullPointerException ex) {

        }
    }


    // User profile
    @RequestMapping(value = "/userprofile", method = RequestMethod.GET)
    public String userProfile(Model model, Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());

        List<Category> categories = recipeService.getAllCategories();

        List<Recipe> favorites = user.getFavorites();

        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> breakfast = new ArrayList<>();
        List<Recipe> lunch = new ArrayList<>();
        List<Recipe> dinner = new ArrayList<>();
        List<Recipe> dessert = new ArrayList<>();

        for (Recipe r: favorites) {
            recipes.add(r);

            switch (r.getCategory()) {
                case BREAKFAST:
                    breakfast.add(r);
                    break;
                case LUNCH:
                    lunch.add(r);
                    break;
                case DINNER:
                    dinner.add(r);
                    break;
                case DESSERT:
                    dessert.add(r);
                    break;
            }
        }


        userUpDownVotes(model, authentication);

        model.addAttribute("user", user);

        model.addAttribute("recipes", favorites);
        model.addAttribute("breakfast", breakfast);
        model.addAttribute("lunch", lunch);
        model.addAttribute("dinner", dinner);
        model.addAttribute("dessert", dessert);

        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", Category.ALL);

        return "profile";
    }

    // Access denies page
    @RequestMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
