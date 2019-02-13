package phan.recipesite.config;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;
import phan.recipesite.service.RecipeService;
import phan.recipesite.service.UserService;

import java.io.Serializable;

// Determines user permission for editing and deleting recipes
@Component
public class RecipePermissionEvaluator implements PermissionEvaluator {

    private final RecipeService recipeService;

    private final UserService userService;

    public RecipePermissionEvaluator(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object
            permission) {

        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        String desiredRoleName = (String) permission;

        if (authentication.isAuthenticated()) {

            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {

                if (StringUtils.equalsIgnoreCase(grantedAuthority.getAuthority(), desiredRoleName)) {

                    return true;

                }
            }
        }

        User user = userService.findByUsername(authentication.getName());

        Long id = (Long) targetId;
        Recipe recipe = recipeService.findById(id);

        return recipe.getUser() != null && recipe.getUser().equals(user);
    }
}