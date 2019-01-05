package phan.recipesite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phan.recipesite.repository.IngredientRepository;
import phan.recipesite.model.Ingredient;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientRepository ingredientDao;

    @Override
    public List<Ingredient> findAll() {
        return (List<Ingredient>) ingredientDao.findAll();
    }

    @Override
    public Ingredient findById(Long id) {
        return ingredientDao.findIngredientById(id);
    }

    @Override
    public void save(Ingredient ingredient) {
        ingredientDao.save(ingredient);
    }

    @Override
    public void delete(Ingredient ingredient) {
        ingredientDao.delete(ingredient);
    }
}
