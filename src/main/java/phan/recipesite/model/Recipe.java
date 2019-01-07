package phan.recipesite.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import phan.recipesite.core.BaseEntity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Recipe extends BaseEntity {
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] image;

    @NotEmpty(message = "Must enter a recipe name")
    private String name;

    @NotEmpty(message = "Must enter a recipe description")
    private String description;

    @Min(value = 1, message = "Preptime must be greater than or equal to 1")
    private int prepTime;

    @Min(value = 1, message = " Preptime must be greater than or equal to 1")
    private int cookTime;

    private String favoritedRecipe;

    @ManyToMany(mappedBy = "favorites")
    private List<User> favoritedBy;

    @NotNull(message = "please select a category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_id")
    @Valid
    private List<Ingredient> ingredients = new ArrayList<>();
    ;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_id")
    @Valid
    private List<Step> steps = new ArrayList<>();
    ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Recipe(RecipeBuilder builder) {
        this.name = builder.name;
        this.category = builder.category;
        this.user = builder.user;
        this.image = builder.image;
        this.description = builder.description;
        this.prepTime = builder.prepTime;
        this.cookTime = builder.cookTime;
        if (builder.ingredients != null) {
            this.ingredients = builder.ingredients;
        }
        if (builder.steps != null) {
            this.steps = builder.steps;
        }
    }

    public Recipe(byte[] image, String name, String description, int prepTime, int cookTime,
                  @NotNull(message = "please select a category") Category category, @Valid List<Ingredient>
                          ingredients, @Valid List<Step> steps) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public List<String> getUserFavorites() {
        List<String> userFavorites = new ArrayList<>();
        HashMap<User, String> map = new HashMap<>();

        if (favoritedBy != null && !favoritedBy.isEmpty()) {
            for (User u : favoritedBy) {
                map.put(u, u.getUsername().toString());

            }
            userFavorites = new ArrayList<>(map.values());
        }
        return userFavorites;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                ", bytes=" + Arrays.toString(image) +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;

        Recipe recipe = (Recipe) o;

        if (prepTime != recipe.prepTime) return false;
        if (cookTime != recipe.cookTime) return false;
        if (!Arrays.equals(image, recipe.image)) return false;
        if (name != null ? !name.equals(recipe.name) : recipe.name != null) return false;
        if (description != null ? !description.equals(recipe.description) : recipe.description != null) return false;
        if (favoritedRecipe != null ? !favoritedRecipe.equals(recipe.favoritedRecipe) : recipe.favoritedRecipe != null)
            return false;
        if (category != recipe.category) return false;
        if (ingredients != null ? !ingredients.equals(recipe.ingredients) : recipe.ingredients != null) return false;
        return steps != null ? steps.equals(recipe.steps) : recipe.steps == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public static class RecipeBuilder {
        private String name;
        private Category category;
        private User user;
        private byte[] image;
        private String description;
        private int prepTime;
        private int cookTime;
        private List<Ingredient> ingredients;
        private List<Step> steps;

        public RecipeBuilder(String name, Category category) {
            this.name = name;
            this.category = category;
        }

        public RecipeBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public RecipeBuilder withImage(byte[] image) {
            this.image = image;
            return this;
        }

        public RecipeBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeBuilder withPrepTime(int prepTime) {
            this.prepTime = prepTime;
            return this;
        }

        public RecipeBuilder withCookTime(int cookTime) {
            this.cookTime = cookTime;
            return this;
        }

        public RecipeBuilder withIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeBuilder withSteps(List<Step> steps) {
            this.steps = steps;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }
    }
}
