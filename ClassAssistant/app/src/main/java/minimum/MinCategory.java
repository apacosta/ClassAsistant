package minimum;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by asmateus on 29/05/17.
 */

public class MinCategory implements Serializable {
    private ArrayList<String> items_descriptions = new ArrayList<>();
    private ArrayList<Integer> items_weights = new ArrayList<>();
    private String id;
    private  String name = "";
    private int weight = 0;

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItemsDescriptions(Iterable<String> i) {
        this.items_descriptions = new ArrayList<>();
        for(String e: i) {
            this.items_descriptions.add(e);
        }
    }

    public void setItemsWeights(Iterable<String> i) {
        this.items_weights = new ArrayList<>();
        for(String e: i) {
            this.items_weights.add(Integer.parseInt(e));
        }
    }

    public String getName() {
        return this.name;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getSumItemsWeights() {
        int sum = 0;
        for(Integer c: this.items_weights) {
            sum += c;
        }

        return sum;
    }

    public ArrayList<String> getItemsDescriptions() {
        return this.items_descriptions;
    }

    public ArrayList<Integer> getItemsWeights() {
        return this.items_weights;
    }
}
