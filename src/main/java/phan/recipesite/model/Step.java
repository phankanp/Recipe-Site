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
public class Step extends BaseEntity {

    @NotEmpty(message = "Must enter step name for every field")
    private String name;

    @ManyToOne
    @NonNull
    private Recipe recipe;

    public Step(String name, Recipe recipe) {
        this.name = name;
        this.recipe = recipe;
    }

}
