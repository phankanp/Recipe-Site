package phan.recipesite.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;
import phan.recipesite.service.RecipeService;
import phan.recipesite.service.UserService;

@Component
public class RecipeValidator implements Validator {
    @Autowired
    private RecipeService RecipeService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Recipe.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Recipe recipe = (Recipe) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
        if (recipe.getName().length() < 3) {
            errors.rejectValue("name", "Size.recipe.name");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty");
        if (recipe.getDescription() == null) {
            errors.rejectValue("description", "NotEmpty");
        }
    }
}
