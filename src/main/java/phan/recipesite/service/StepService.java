package phan.recipesite.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phan.recipesite.model.Step;
import phan.recipesite.repository.StepRepository;

@Service
public class StepService {

    private final StepRepository stepRepository;

    @Autowired
    public StepService(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }


    public void save(Step step) {
        stepRepository.save(step);
    }


    public void delete(Step step) {
        stepRepository.delete(step);
    }
}
