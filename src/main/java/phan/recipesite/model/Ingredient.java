package phan.recipesite.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Ingredient extends BaseEntity {

    @NotEmpty(message = "Must enter an ingredient name for every field")
    private String name;

    @NotEmpty(message = "Must enter an ingredient ingredient condition for every field")
    private String ingredientCondition;

    @NotEmpty(message = "Must enter an ingredient quantity for every field")
    private String quantity;

    public Ingredient(String name, String ingredientCondition, String quantity) {
        this();
        this.name = name;
        this.ingredientCondition = ingredientCondition;
        this.quantity = quantity;
    }

}
