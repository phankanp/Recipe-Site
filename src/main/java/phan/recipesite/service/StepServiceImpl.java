package phan.recipesite.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phan.recipesite.model.Step;
import phan.recipesite.repository.StepRepository;

@Service
public class StepServiceImpl implements StepService {

    private final StepRepository stepRepository;

    @Autowired
    public StepServiceImpl(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public void save(Step step) {
        stepRepository.save(step);
    }

    @Override
    public void delete(Step step) {
        stepRepository.delete(step);
    }
}
