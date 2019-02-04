package phan.recipesite.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import phan.recipesite.model.User;
import phan.recipesite.model.validator.PasswordValidator;
import phan.recipesite.service.UserService;
import phan.recipesite.web.FlashMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // User sign up form
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }

        return "/signup";
    }

    // Add new user
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        passwordValidator.validate(user, result);


        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);


            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);


            return "redirect:/signup";
        }


        try {
            userService.save(user);

        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Successfully not created account! Please " +
                    "login.",
                    FlashMessage.Status.FAILURE));
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/signup";
        }

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Successfully created account! Please login.",
                FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }

    // Login form
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
            // "flash" session attribute must not exist. Do nothing and proceed normally
        }
        return "login";
    }

    // User profile
    @RequestMapping(value = "/userprofile", method = RequestMethod.GET)
    public String userProfile(Model model, Authentication authentication) {

        //User user = userService.findByUsername(authentication.getName());

        //model.addAttribute("user", user);

        return "profile";
    }

    // Access denies page
    @RequestMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
