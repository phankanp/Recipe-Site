package phan.recipesite.model;

import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;

@Entity
public class Step extends BaseEntity {
    private String name;

    public Step() {
        super();
    }

    public Step(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Step{" +
                ", name='" + name + '\'' +
                '}';
    }
}
