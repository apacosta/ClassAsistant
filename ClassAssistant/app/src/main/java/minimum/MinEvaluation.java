package minimum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import io.DBRepresentation;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinEvaluation implements Serializable {
    private String id;
    private String name;
    private MinRubric rubric;
    private int weight = 100;

    public MinEvaluation() {

    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setRubric(MinRubric rubric) {
        this.rubric = rubric;
    }

    public MinRubric getRubric() {
        return this.rubric;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }
}
