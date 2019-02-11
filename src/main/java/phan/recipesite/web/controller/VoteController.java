package phan.recipesite.web.controller;

import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import phan.recipesite.model.Recipe;
import phan.recipesite.model.Vote;
import phan.recipesite.service.RecipeService;
import phan.recipesite.service.VoteService;

@RestController
public class VoteController {

    private VoteService voteService;
    private RecipeService recipeService;

    @Autowired
    public VoteController(VoteService voteService, RecipeService recipeService) {
        this.voteService = voteService;
        this.recipeService = recipeService;
    }

    @RequestMapping(value = "/vote/recipe/{recipeId}/direction/{direction}/votecount/{voteCount}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public int vote(@PathVariable Long recipeId, @PathVariable short direction, @PathVariable int voteCount) {
        Recipe recipe = recipeService.findById(recipeId);

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
