package phan.recipesite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phan.recipesite.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
