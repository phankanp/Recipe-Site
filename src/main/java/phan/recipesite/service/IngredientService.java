package phan.recipesite.service;

import org.springframework.stereotype.Service;
import phan.recipesite.model.Ingredient;
import phan.recipesite.repository.IngredientRepository;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll() {
        return (List<Ingredient>) ingredientRepository.findAll();
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findIngredientById(id);
    }

    public void save(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

    public void delete(Ingredient ingredient) {
        ingredientRepository.delete(ingredient);
    }
}
