package phan.recipesite.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import phan.recipesite.model.Ingredient;

@Repository
public interface IngredientDao extends CrudRepository<Ingredient, Long> {
    Ingredient findIngredientById(Long id);

    Ingredient findByName(String name);
}
