package phan.recipesite.model;

import lombok.*;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Ingredient extends BaseEntity {

    @NotEmpty(message = "Must enter an ingredient name for every field")
    private String name;

    @NotEmpty(message = "Must enter an ingredient ingredient condition for every field")
    private String ingredientCondition;

    @NotEmpty(message = "Must enter an ingredient quantity for every field")
    private String quantity;

    @ManyToOne
    @NonNull
    private Recipe recipe;

    public Ingredient(String name, String ingredientCondition, String quantity, Recipe recipe) {
        this.name = name;
        this.ingredientCondition = ingredientCondition;
        this.quantity = quantity;
        this.recipe = recipe;
    }
}
