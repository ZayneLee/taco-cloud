package tacos;

import java.util.List;

import lombok.Data;

@Data
public class Taco {

    private String name;
    private List<Ingredient> ingredients;
}
