package phan.recipesite.service;

import phan.recipesite.model.Ingredient;

import java.util.List;

public interface IngredientService {
    List<Ingredient> findAll();

    Ingredient findById(Long id);

    void save(Ingredient ingredient);

    void delete(Ingredient ingredient);
}
