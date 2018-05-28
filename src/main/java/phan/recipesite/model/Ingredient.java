package phan.recipesite.model;

import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;

@Entity
public class Ingredient extends BaseEntity {
    private String name;

    private String ingredientCondition;

    private String quantity;

    public Ingredient() {
        super();
    }

    public Ingredient(String name, String ingredientCondition, String quantity) {
        this();
        this.name = name;
        this.ingredientCondition = ingredientCondition;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredientCondition() {
        return ingredientCondition;
    }

    public void setIngredientCondition(String ingredientCondition) {
        this.ingredientCondition = ingredientCondition;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
