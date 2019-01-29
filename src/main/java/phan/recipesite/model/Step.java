package phan.recipesite.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import phan.recipesite.core.BaseEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString
public class Step extends BaseEntity {

    @NotEmpty(message = "Must enter step name for every field")
    private String name;

    public Step(String name) {
        this();
        this.name = name;
    }

}
