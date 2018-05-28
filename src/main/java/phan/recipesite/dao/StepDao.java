package phan.recipesite.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import phan.recipesite.model.Step;

@Repository
public interface StepDao extends CrudRepository<Step, Long> {
}
