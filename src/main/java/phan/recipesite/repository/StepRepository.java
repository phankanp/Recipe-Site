package phan.recipesite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import phan.recipesite.model.Step;


public interface StepRepository extends JpaRepository<Step, Long> {
}
