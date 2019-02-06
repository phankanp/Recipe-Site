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
@RequiredArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {

    @NonNull
    private String body;

    @ManyToOne
    @NonNull
    private Recipe recipe;

}
