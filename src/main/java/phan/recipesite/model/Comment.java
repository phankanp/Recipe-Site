package phan.recipesite.model;

import lombok.*;
import org.springframework.boot.CommandLineRunner;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @NonNull
    private String body;

    @ManyToOne
    @NonNull
    private Recipe recipe;

    @NonNull
    private String createdBy;

    public Comment(@NonNull String body, Recipe recipe) {
        this.body = body;
        this.recipe = recipe;
    }
}
