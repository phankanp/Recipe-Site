package phan.recipesite.model;

import lombok.*;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Vote extends BaseEntity {

    private short direction;

    @NonNull
    @ManyToOne
    private Recipe recipe;

    public Vote(short direction, @NonNull Recipe recipe) {
        this.direction = direction;
        this.recipe = recipe;
    }
}
