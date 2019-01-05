package phan.recipesite.model;

import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
public class Ingredient extends BaseEntity {

    @NotEmpty(message = "Must enter an ingredient name for every field")
    private String name;

    @NotEmpty(message = "Must enter an ingredient ingredient condition for every field")
    private String ingredientCondition;

    @NotEmpty(message = "Must enter an ingredient quantity for every field")
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

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", ingredientCondition='" + ingredientCondition + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;

        Ingredient that = (Ingredient) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (ingredientCondition != null ? !ingredientCondition.equals(that.ingredientCondition) : that
                .ingredientCondition != null)
            return false;
        return quantity != null ? quantity.equals(that.quantity) : that.quantity == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (ingredientCondition != null ? ingredientCondition.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }
}
