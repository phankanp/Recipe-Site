package phan.recipesite.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import phan.recipesite.model.Category;
import phan.recipesite.model.Recipe;

import java.util.List;

@Repository
public interface RecipeDao extends CrudRepository<Recipe, Long> {

    List<Recipe> findAll();

    @Query("select r from Recipe r where r.user.id=:id")
    List<Recipe> findByUser(@Param("id") Long id);

    Recipe findRecipeById(Long id);

    List<Recipe> findByCategory(Category category);

    List<Recipe> findByDescriptionContainingOrIngredientsNameIgnoreCase(String search, String search2);

}
