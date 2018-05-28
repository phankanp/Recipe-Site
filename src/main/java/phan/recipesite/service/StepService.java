package phan.recipesite.service;

import phan.recipesite.model.Step;

public interface StepService {
    void save(Step step);

    void delete(Step step);
}
