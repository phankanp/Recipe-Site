package phan.recipesite.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import phan.recipesite.dao.RecipeDao;
import phan.recipesite.dao.RoleDao;
import phan.recipesite.dao.UserDao;
import phan.recipesite.model.*;
import phan.recipesite.service.UserService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
public class DatabaseLoader implements ApplicationRunner {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private final RecipeDao recipeDao;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserService userService;

    @Autowired
    public DatabaseLoader(RecipeDao recipeDao, UserDao userDao, RoleDao roleDao, UserService userService) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userService = userService;
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

        User testUser1 = new User("user1", PASSWORD_ENCODER.encode("password"), PASSWORD_ENCODER.encode("password"));
        User testUser2 = new User("user2", PASSWORD_ENCODER.encode("password"), PASSWORD_ENCODER.encode("password"));

        testUser1.setRoles(role1);
        testUser2.setRoles(role2);


        userDao.save(testUser1);
        userDao.save(testUser2);

        List<Ingredient> ingredient1 = new ArrayList<>(Arrays.asList(
                new Ingredient("Eggs", "Fresh", "3"),
                new Ingredient("Milk", "Fresh", "1 Cup"),
                new Ingredient("Pancake Mix", "Fresh", "1 Cup")
        ));

        List<Ingredient> ingredient2 = new ArrayList<>(Arrays.asList(
                new Ingredient("Eggs", "Fresh", "2"),
                new Ingredient("Chocolate", "Dark", ".5 cup"),
                new Ingredient("Milk", "Fresh", "1 Cup"),
                new Ingredient("Brownie Mix", "Fresh", "1 Cup")
        ));

        List<Step> step1 = new ArrayList<>(Arrays.asList(
                new Step("Heat oven"),
                new Step("Mix ingredients"),
                new Step("Bake")
        ));

        List<Step> step2 = new ArrayList<>(Arrays.asList(
                new Step("Heat oven"),
                new Step("Mix ingredients"),
                new Step("Bake")
        ));

        List<Recipe> recipes = Arrays.asList(
                new Recipe.RecipeBuilder("Pancakes", Category.BREAKFAST)
                        .withDescription("Delicious Pancakes")
                        .withPrepTime(10)
                        .withCookTime(15)
                        .withIngredients(ingredient1)
                        .withSteps(step1)
                        .withImage(Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/pancakes" +
                                ".png")))
                        .withUser(testUser1)
                        .build(),

                new Recipe.RecipeBuilder("Brownies", Category.DESSERT)
                        .withDescription("Amazing Brownies")
                        .withPrepTime(20)
                        .withCookTime(60)
                        .withIngredients(ingredient2)
                        .withSteps(step2)
                        .withImage(Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/brownies" +
                                ".png")))
                        .withUser(testUser2)
                        .build()
        );

        userService.toggleFavorite(testUser1, recipes.get(0));

        recipes.forEach(recipeDao::save);
        userDao.save(testUser1);
    }
}
