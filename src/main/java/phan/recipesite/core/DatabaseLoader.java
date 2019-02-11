package phan.recipesite.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import phan.recipesite.model.*;
import phan.recipesite.repository.RecipeRepository;
import phan.recipesite.repository.RoleRepository;
import phan.recipesite.repository.UserRepository;
import phan.recipesite.service.CommentService;
import phan.recipesite.service.IngredientService;
import phan.recipesite.service.StepService;
import phan.recipesite.service.UserService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
public class DatabaseLoader implements ApplicationRunner {
    public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private final RecipeRepository recipeDao;
    private final UserRepository userDao;
    private final RoleRepository roleDao;
    private final UserService userService;
    private final CommentService commentService;
    private final StepService stepService;
    private final IngredientService ingredientService;

    @Autowired
    public DatabaseLoader(RecipeRepository recipeDao, UserRepository userDao, RoleRepository roleDao, UserService
            userService, CommentService commentService, StepService stepService, IngredientService ingredientService) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userService = userService;
        this.commentService = commentService;
        this.stepService = stepService;
        this.ingredientService = ingredientService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Set<Role> role1 = new HashSet<>();
        Set<Role> role2 = new HashSet<>();

        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        roleDao.save(roleAdmin);
        roleDao.save(roleUser);

        role1.add(roleAdmin);
        role1.add(roleUser);
        role2.add(roleUser);

        String secret = PASSWORD_ENCODER.encode("password");

        User testUser1 = new User("Admin", "User", "AdminUser@gmail.com", "AdminUser", secret, secret);
        User testUser2 = new User("Role", "User", "RoleUser@gmail.com", "RoleUser", secret, secret);

        testUser1.setRoles(role1);
        testUser2.setRoles(role2);

        testUser1.setEnabled(true);

        userDao.save(testUser1);
        userDao.save(testUser2);

//        List<Ingredient> ingredient1 = new ArrayList<>(Arrays.asList(
//                new Ingredient("Eggs", "Fresh", "3"),
//                new Ingredient("Milk", "Fresh", "1 Cup"),
//                new Ingredient("Pancake Mix", "Fresh", "1 Cup")
//        ));
//
//        List<Ingredient> ingredient2 = new ArrayList<>(Arrays.asList(
//                new Ingredient("Eggs", "Fresh", "2"),
//                new Ingredient("Chocolate", "Dark", ".5 cup"),
//                new Ingredient("Milk", "Fresh", "1 Cup"),
//                new Ingredient("Brownie Mix", "Fresh", "1 Cup")
//        ));

//        List<Step> step1 = new ArrayList<>(Arrays.asList(
//                new Step("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi semper orci sit amet turpis lobortis, non semper ex tincidunt. Etiam diam nunc, blandit id molestie mollis, convallis eu ex."),
//                new Step("Sed volutpat, nibh nec maximus ultrices, mauris enim rutrum elit, sed porta dui metus quis mauris."),
//                new Step("Phasellus vulputate interdum eros vehicula ultrices. Aenean hendrerit dapibus ex, at sodales erat pharetra pharetra. ")
//        ));
//
//        List<Step> step2 = new ArrayList<>(Arrays.asList(
//                new Step("Heat oven"),
//                new Step("Mix ingredients"),
//                new Step("Bake")
//        ));

//        List<Comment> comments1 = new ArrayList<>(Arrays.asList(
//                new Comment("Test Comment 1", testUser1.getUsername()),
//                new Comment("Test Comment 2", testUser2.getUsername())
//        ));
//
//        List<Comment> comments2 = new ArrayList<>(Arrays.asList(
//                new Comment("Test Comment 3", testUser1.getUsername()),
//                new Comment("Test Comment 4", testUser2.getUsername())
//        ));
/***************************************** Recipe 1 *******************************************************************/
        Recipe recipe = new Recipe(
                Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/pancakes" +
                        ".jpg")),
                "Pancakes",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent maximus " +
                        "commodo neque, in semper mauris suscipit et. Aliquam sem velit, sodales eget justo " +
                        "efficitur, commodo sollicitudin quam. Donec vitae quam vitae mi dictum malesuada. " +
                        "Mauris sodales vulputate tortor. Maecenas sit amet pretium nibh, eget aliquam massa." +
                        " Nulla et elementum neque. Quisque maximus iaculis tempus. Maecenas id sem sed dui " +
                        "dignissim sagittis.",
                10,
                15,
                Category.BREAKFAST
        );

        recipe.setUser(testUser1);

        recipeDao.save(recipe);
//
//        List<Recipe> recipes = new ArrayList<>();
//
//        recipes.add(recipe);
//
//        recipes.forEach(recipeDao::save);

        Ingredient ingredient1 = new Ingredient("eggs", "scrambled", "3", recipe);
        Ingredient ingredient2 = new Ingredient("milk", "fresh", "1 Cup", recipe);
        Ingredient ingredient3 = new Ingredient("pancake mix", "fresh", "1 Cup", recipe);
        Ingredient ingredients[] = {ingredient1, ingredient2, ingredient3};
        for (Ingredient ingredient : ingredients) {
            ingredientService.save(ingredient);
            recipe.addIngredient(ingredient);
        }

        Step step1 = new Step("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi semper orci sit" +
                " amet turpis lobortis, non semper ex tincidunt. Etiam diam nunc, blandit id molestie mollis, convallis eu" +
                " ex.", recipe);
        Step step2 = new Step("Sed volutpat, nibh nec maximus ultrices, mauris enim rutrum elit, sed porta dui metus " +
                "quis mauris.", recipe);
        Step step3 = new Step("Phasellus vulputate interdum eros vehicula ultrices. Aenean hendrerit dapibus ex, at " +
                "sodales erat pharetra pharetra.", recipe);
        Step steps[] = {step1, step2, step3};
        for(Step step : steps) {
            stepService.save(step);
            recipe.addStep(step);
        }

        Comment spring = new Comment("Thank you for this recipe!",recipe);
        Comment security = new Comment("Delicious pancakes!",recipe);
        Comment pwa = new Comment("Amazing choice of ingredients!",recipe);
        Comment comments[] = {spring,security,pwa};
        addUserToComment(testUser1, testUser2, recipe, comments);

        userService.toggleFavorite(testUser1, recipe);

        userDao.save(testUser1);

/***************************************** Recipe 2 *******************************************************************/

        Recipe brownies = new Recipe(
                Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/brownies" +
                        ".jpg")),
                "Brownies",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent maximus " +
                        "commodo neque, in semper mauris suscipit et. Aliquam sem velit, sodales eget justo " +
                        "efficitur, commodo sollicitudin quam. Donec vitae quam vitae mi dictum malesuada. " +
                        "Mauris sodales vulputate tortor. Maecenas sit amet pretium nibh, eget aliquam massa." +
                        " Nulla et elementum neque. Quisque maximus iaculis tempus. Maecenas id sem sed dui " +
                        "dignissim sagittis.",
                20,
                60,
                Category.DESSERT
        );

        brownies.setUser(testUser2);

        recipeDao.save(brownies);

        Ingredient browniesIngredient1 = new Ingredient("eggs", "scrambled", "2", brownies);
        Ingredient browniesIngredient2 = new Ingredient("dark chocolate", "cut", "0.5 cup", brownies);
        Ingredient browniesIngredient3 = new Ingredient("milk", "fresh", "1 Cup", brownies);
        Ingredient browniesIngredient4 = new Ingredient("brownie mix", "fresh", "1 cup", brownies);
        Ingredient browniesIngredients[] = {browniesIngredient1, browniesIngredient2, browniesIngredient3};
        for (Ingredient ingredient : browniesIngredients) {
            ingredientService.save(ingredient);
            brownies.addIngredient(ingredient);
        }

        Step browniesStep1 = new Step("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi semper orci sit" +
                " amet turpis lobortis, non semper ex tincidunt. Etiam diam nunc, blandit id molestie mollis, convallis eu" +
                " ex.", brownies);
        Step browniesStept2 = new Step("Sed volutpat, nibh nec maximus ultrices, mauris enim rutrum elit, sed porta dui metus " +
                "quis mauris.", brownies);
        Step browniesStep3 = new Step("Phasellus vulputate interdum eros vehicula ultrices. Aenean hendrerit dapibus ex, at " +
                "sodales erat pharetra pharetra.", brownies);
        Step browniesSteps[] = {browniesStep1, browniesStept2, browniesStep3};
        for(Step step : browniesSteps) {
            stepService.save(step);
            brownies.addStep(step);
        }

        Comment browniesComment1 = new Comment("Thank you for this recipe!",brownies);
        Comment browniesComment2 = new Comment("Delicious brownies!",brownies);
        Comment browniesComment3 = new Comment("Amazing choice of ingredients!",brownies);
        Comment browniesComments[] = {browniesComment1,browniesComment2,browniesComment3};
        addUserToComment(testUser1, testUser2, brownies, browniesComments);

        userDao.save(testUser2);

/***************************************** Recipe 3 *******************************************************************/

        Recipe sandwich = new Recipe(
                Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/sandwich" +
                        ".jpg")),
                "Chicken Sandwich",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent maximus " +
                        "commodo neque, in semper mauris suscipit et. Aliquam sem velit, sodales eget justo " +
                        "efficitur, commodo sollicitudin quam. Donec vitae quam vitae mi dictum malesuada. " +
                        "Mauris sodales vulputate tortor. Maecenas sit amet pretium nibh, eget aliquam massa." +
                        " Nulla et elementum neque. Quisque maximus iaculis tempus. Maecenas id sem sed dui " +
                        "dignissim sagittis.",
                20,
                30,
                Category.LUNCH
        );

        sandwich.setUser(testUser1);

        recipeDao.save(sandwich);

        Ingredient sandwichIngredient1 = new Ingredient("boneless chicken breast ", "skinless", "4", sandwich);
        Ingredient sandwichIngredient2 = new Ingredient("barbeque sauce", "tangy", "0.5 cup", sandwich);
        Ingredient sandwichIngredient3 = new Ingredient("Swiss cheese", "slices", "4", sandwich);
        Ingredient sandwichIngredients[] = {sandwichIngredient1, sandwichIngredient2, sandwichIngredient3};
        for (Ingredient ingredient : sandwichIngredients) {
            ingredientService.save(ingredient);
            brownies.addIngredient(ingredient);
        }

        Step sandwichStep1 = new Step("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi semper orci sit" +
                " amet turpis lobortis, non semper ex tincidunt. Etiam diam nunc, blandit id molestie mollis, convallis eu" +
                " ex.", sandwich);
        Step sandwichStep2 = new Step("Sed volutpat, nibh nec maximus ultrices, mauris enim rutrum elit, sed porta dui metus " +
                "quis mauris.", sandwich);
        Step sandwichStep3 = new Step("Phasellus vulputate interdum eros vehicula ultrices. Aenean hendrerit dapibus ex, at " +
                "sodales erat pharetra pharetra.", sandwich);
        Step sandwichSteps[] = {sandwichStep1, sandwichStep2, sandwichStep3};
        for(Step step : sandwichSteps) {
            stepService.save(step);
            brownies.addStep(step);
        }

        Comment sandwichComment1 = new Comment("Thank you for this recipe!",sandwich);
        Comment sandwichComment2 = new Comment("Delicious sandwich!",sandwich);
        Comment sandwichComment3 = new Comment("Amazing choice of ingredients!",sandwich);
        Comment sandwichComments[] = {sandwichComment1,sandwichComment2,sandwichComment3};
        addUserToComment(testUser1, testUser2, brownies, sandwichComments);

        userDao.save(testUser1);

/***************************************** Recipe 4 *******************************************************************/

        Recipe pasta = new Recipe(
                Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/pasta" +
                        ".jpg")),
                "Chicken Pasta",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent maximus " +
                        "commodo neque, in semper mauris suscipit et. Aliquam sem velit, sodales eget justo " +
                        "efficitur, commodo sollicitudin quam. Donec vitae quam vitae mi dictum malesuada. " +
                        "Mauris sodales vulputate tortor. Maecenas sit amet pretium nibh, eget aliquam massa." +
                        " Nulla et elementum neque. Quisque maximus iaculis tempus. Maecenas id sem sed dui " +
                        "dignissim sagittis.",
                20,
                20,
                Category.DINNER
        );

        pasta.setUser(testUser2);

        recipeDao.save(pasta);

        Ingredient pastaIngredient1 = new Ingredient("boneless chicken breast ", "skinless", "4", pasta);
        Ingredient pastaIngredient2 = new Ingredient("pasta", "sprial", "4 ounces", pasta);
        Ingredient pastaIngredient3 = new Ingredient("cajun seasoning", "ground", "2 tsp", pasta);
        Ingredient pastahIngredients[] = {pastaIngredient1, pastaIngredient2, pastaIngredient3};
        for (Ingredient ingredient : pastahIngredients) {
            ingredientService.save(ingredient);
            brownies.addIngredient(ingredient);
        }

        Step pastaStep1 = new Step("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi semper orci sit" +
                " amet turpis lobortis, non semper ex tincidunt. Etiam diam nunc, blandit id molestie mollis, convallis eu" +
                " ex.", pasta);
        Step pastaStep2 = new Step("Sed volutpat, nibh nec maximus ultrices, mauris enim rutrum elit, sed porta dui metus " +
                "quis mauris.", pasta);
        Step pastaStep3 = new Step("Phasellus vulputate interdum eros vehicula ultrices. Aenean hendrerit dapibus ex, at " +
                "sodales erat pharetra pharetra.", pasta);
        Step pastaSteps[] = {pastaStep1, pastaStep2, pastaStep3};
        for(Step step : pastaSteps) {
            stepService.save(step);
            brownies.addStep(step);
        }

        Comment pastaComment1 = new Comment("Thank you for this recipe!", pasta);
        Comment pastaComment2 = new Comment("Delicious pasta!", pasta);
        Comment pastaComment3 = new Comment("Amazing choice of ingredients!", pasta);
        Comment pastaComments[] = {pastaComment1,pastaComment2,pastaComment3};
        addUserToComment(testUser1, testUser2, brownies, pastaComments);

        userDao.save(testUser2);

    }

    private void addUserToComment(User testUser1, User testUser2, Recipe brownies, Comment[] pastaComments) {
        for(Comment comment : pastaComments) {
            if (comment.getBody().startsWith("Thank")|| comment.getBody().startsWith("Amazing")) {
                comment.setCreatedBy(testUser1.getUsername());
            } else {
                comment.setCreatedBy(testUser2.getUsername());
            }
            commentService.save(comment);
            brownies.addComment(comment);
        }
    }
}
