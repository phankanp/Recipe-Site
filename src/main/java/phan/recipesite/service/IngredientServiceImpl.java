package phan.recipesite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phan.recipesite.model.Ingredient;
import phan.recipesite.repository.IngredientRepository;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> findAll() {
        return (List<Ingredient>) ingredientRepository.findAll();
    }

    @Override
    public Ingredient findById(Long id) {
        return ingredientRepository.findIngredientById(id);
    }

    @Override
    public void save(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

    @Override
    public void delete(Ingredient ingredient) {
        ingredientRepository.delete(ingredient);
    }
}
