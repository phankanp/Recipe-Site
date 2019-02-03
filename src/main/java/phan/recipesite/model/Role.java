package phan.recipesite.model;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Role extends BaseEntity {

    @NonNull
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
}