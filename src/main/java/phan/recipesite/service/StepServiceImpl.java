package phan.recipesite.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phan.recipesite.dao.StepDao;
import phan.recipesite.model.Step;

@Service
public class StepServiceImpl implements StepService {
    @Autowired
    private StepDao stepDao;

    @Override
    public void save(Step step) {
        stepDao.save(step);
    }

    @Override
    public void delete(Step step) {
        stepDao.delete(step);
    }
}
