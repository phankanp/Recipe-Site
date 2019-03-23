package phan.recipesite.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import phan.recipesite.core.BaseEntity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Recipe extends BaseEntity {
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] image;

    @NotEmpty(message = "Must enter a recipe name")
    private String name;

    @NotEmpty(message = "Must enter a recipe description")
    @Column(length = 1024)
    private String description;

    @Min(value = 1, message = "Prep time must be greater than or equal to 1")
    private int prepTime;

    @Min(value = 1, message = "Cook time must be greater than or equal to 1")
    private int cookTime;

    private String favoritedRecipe;

    @ManyToMany(mappedBy = "favorites")
    private List<User> favoritedBy;

    @NotNull(message = "please select a category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "recipe")
    @Valid
    private List<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    @Valid
    private List<Step> steps = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    private List<Vote> votes = new ArrayList<>();

    private int voteCount = 0;

    @OneToMany(mappedBy = "recipe")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Recipe(byte[] image, String name, String description, int prepTime, int cookTime,
                  @NotNull(message = "please select a category") Category category) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.category = category;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
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

//    public List<String> ingredientSearch;
//
//    public void setIngredientSearch(List<Ingredient> ingredients) {
//        List<String> results = new ArrayList<>();
//
//        for (Ingredient i : ingredients) {
//            results.add(i.toString());
//        }
//
//        ingredientSearch = results;
//    }
}
