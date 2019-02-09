package phan.recipesite.model;


import lombok.*;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
@Getter
@Setter
@RequiredArgsConstructor

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Role extends BaseEntity {

    @NonNull
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
}