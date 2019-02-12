package phan.recipesite.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Must enter a first name")
    @Size(min = 2, message = "First name must be at least two characters")
    private String firstName;

    @NotEmpty(message = "Must enter a last name")
    @Size(min = 2, message = "Last name must be at least two characters")
    private String lastName;

    @NotEmpty(message = "Must enter an email address")
    @Size(min = 8, max = 20, message = "Email must be between 8 and 20 characters")
    @Column(unique = true)
    @Email(message = "Must enter a valid email address")
    private String email;

    @NotEmpty(message = "Must enter a username")
    @Size(min = 8, max = 20, message = "Username must be between 8 and 20 characters")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Must enter a password")
    @Size(min = 6, max = 60, message = "Password must be between 6 and 60 characters")
    private String password;

    @Transient
    @NotEmpty(message = "Please enter Password Confirmation.")
    private String passwordConfirm;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> favorites;


    public User(String firstName, String lastName, String email, String username, String password, String
            passwordConfirm) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        favorites = new ArrayList<>();
        setPassword(password);
        setPasswordConfirm(passwordConfirm);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addRoles(Set<Role> roles) {
        roles.forEach(this::addRole);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
