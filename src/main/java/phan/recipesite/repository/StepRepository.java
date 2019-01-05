package phan.recipesite.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import phan.recipesite.model.Step;

@Repository
public interface StepRepository extends CrudRepository<Step, Long> {
}
