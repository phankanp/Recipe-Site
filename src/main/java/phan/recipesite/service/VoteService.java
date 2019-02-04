package phan.recipesite.service;

import org.springframework.stereotype.Service;
import phan.recipesite.model.Vote;
import phan.recipesite.repository.VoteRepository;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }
}