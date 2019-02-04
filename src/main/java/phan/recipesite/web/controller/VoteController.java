package phan.recipesite.web.controller;

import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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

    @Secured({"ROLE_USER"})
    @GetMapping("/vote/recipe/{recipeId}/direction/{direction}/votecount/{voteCount}")
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
