package phan.recipesite.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import phan.recipesite.model.Ingredient;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    Ingredient findIngredientById(Long id);

    Ingredient findByName(String name);
}
