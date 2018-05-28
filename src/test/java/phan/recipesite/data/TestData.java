package phan.recipesite.data;

import phan.recipesite.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestData {

    public static List<Ingredient> ingredient1 = new ArrayList<>(Arrays.asList(
            new Ingredient("Eggs", "Fresh", "3"),
            new Ingredient("Milk", "Fresh", "1 Cup"),
            new Ingredient("Pancake Mix", "Fresh", "1 Cup")
    ));
    public static List<Ingredient> ingredient2 = new ArrayList<>(Arrays.asList(
            new Ingredient("Eggs", "Fresh", "2"),
            new Ingredient("Chocolate", "Dark", ".5 cup"),
            new Ingredient("Milk", "Fresh", "1 Cup"),
            new Ingredient("Brownie Mix", "Fresh", "1 Cup")
    ));
    public static List<Step> step1 = new ArrayList<>(Arrays.asList(
            new Step("Heat oven"),
            new Step("Mix ingredients"),
            new Step("Bake")
    ));
    public static List<Step> step2 = new ArrayList<>(Arrays.asList(
            new Step("Heat oven"),
            new Step("Mix ingredients"),
            new Step("Bake")
    ));
    public static List<Recipe> recipesList = Arrays.asList(
            new Recipe.RecipeBuilder("Pancaakes", Category.BREAKFAST)
                    .withDescription("Delicious Pancakes")
                    .withPrepTime(10)
                    .withCookTime(15)
                    .withIngredients(ingredient1)
                    .withSteps(step1)
                    .withUser(admin())
                    .build(),

            new Recipe.RecipeBuilder("Brownies", Category.DESSERT)
                    .withDescription("Amazing Brownies")
                    .withPrepTime(20)
                    .withCookTime(60)
                    .withIngredients(ingredient2)
                    .withSteps(step2)
                    .withUser(user1())
                    .build()
    );

    public static User user1() {
        User user = new User("user1", "password", "password");

        return user;
    }

    public static User admin() {
        User admin = new User("admin", "password", "password");

        return admin;
    }

}
