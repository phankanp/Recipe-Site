package phan.recipesite.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Vote extends BaseEntity {
    @NonNull
    private short direction;

    @NonNull
    @ManyToOne
    private Recipe recipe;

}
