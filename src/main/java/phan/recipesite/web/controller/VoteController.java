package phan.recipesite.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import phan.recipesite.model.Category;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.User;
import phan.recipesite.model.Vote;
import phan.recipesite.service.RecipeService;
import phan.recipesite.service.UserService;
import phan.recipesite.service.VoteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VoteController {

    private VoteService voteService;
    private RecipeService recipeService;
    private UserService userService;

    @Autowired
    public VoteController(VoteService voteService, RecipeService recipeService, UserService userService) {
        this.voteService = voteService;
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @RequestMapping(value = "/vote/recipe/{recipeId}/direction/{direction}/votecount/{voteCount}", method =
            RequestMethod.GET)
    public int vote(@PathVariable Long recipeId, @PathVariable short direction, @PathVariable int voteCount, Authentication authentication) {
        Recipe recipe = recipeService.findById(recipeId);
        User user = userService.findByUsername(authentication.getName());

        userService.upVoteDownVoteTracker(user, recipeId, direction);

        if (recipe != null) {
            Vote vote = new Vote(direction, recipe);
            voteService.save(vote);

            int updatedVoteCount = voteCount + direction;
            recipe.setVoteCount(updatedVoteCount);
            recipeService.save(recipe);
            return updatedVoteCount;
        }
        return voteCount;
    }
}
