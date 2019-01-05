package phan.recipesite.model;


import org.springframework.security.core.GrantedAuthority;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Role extends BaseEntity implements GrantedAuthority {
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {
        super();
    }

    public Role(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}