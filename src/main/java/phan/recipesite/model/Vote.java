package phan.recipesite.model;

import lombok.*;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Vote extends BaseEntity {
    @NonNull
    private short direction;

    @NonNull
    @ManyToOne
    private Recipe recipe;

}
