package minimum;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinRubric implements Serializable {
    private ArrayList<MinCategory> categories = new ArrayList<>();
    private final String id;
    private int sum_weights = 0;
    private String name = "";
    private ArrayList<Integer> label_fields = new ArrayList<>();
    private ArrayList<Integer> weight_fields = new ArrayList<>();
    private ArrayList<Double> score_fields = new ArrayList<>();
    private ArrayList<String> label_ids = new ArrayList<>();

    public MinRubric(String id) {
        this.id = id;
    }

    public int getSumWeights() {
        int sum = 0;
        for(MinCategory c: categories) {
            sum += c.getWeight();
        }
        this.sum_weights = sum;
        return this.sum_weights;
    }

    public void resetFields() {
        this.label_fields = new ArrayList<>();
        this.label_ids = new ArrayList<>();
        this.weight_fields = new ArrayList<>();
        this.score_fields = new ArrayList<>();
    }

    public void setSumWeights(int sum_weights) {
        this.sum_weights = sum_weights;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public ArrayList<Integer> getLabelFields() {
        return this.label_fields;
    }

    public ArrayList<Double> getScoreFields() {
        return this.score_fields;
    }

    public ArrayList<Integer> getWeightFields() {
        return this.weight_fields;
    }

    public ArrayList<String> getLabelIDs() {
        return this.label_ids;
    }

    public ArrayList<MinCategory> getCategories() {
        return this.categories;
    }

    public void setCategories(Iterable<MinCategory> i) {
        this.categories = new ArrayList<>();

        for(MinCategory c: i) {
            this.categories.add(c);
        }
    }

    public boolean checkSumWeights() {
        int sum = 0;
        for(MinCategory c: categories) {
            sum += c.getWeight();
        }

        if(sum == 100)
            return true;
        else
            return false;
    }
}
