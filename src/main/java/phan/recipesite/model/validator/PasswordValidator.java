package phan.recipesite.model.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import phan.recipesite.model.User;
import phan.recipesite.service.UserService;

@Component
public class PasswordValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty");
        if (user.getUsername().length() < 2) {
            errors.rejectValue("firstName", "Size.user.firstName");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty");
        if (user.getUsername().length() < 2) {
            errors.rejectValue("lastName", "Size.user.lastName");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 8 || user.getUsername().length() > 20) {
            errors.rejectValue("username", "Size.user.username");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.user.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 6 || user.getPassword().length() > 60) {
            errors.rejectValue("password", "Size.user.password");
        }

        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            errors.rejectValue("password", "Diff.user.passwordConfirm");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.user.passwordConfirm");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (userService.findByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate.user.email");
        }
    }
}
