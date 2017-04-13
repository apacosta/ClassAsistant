package minimum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Signature;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinSignature implements Serializable {

    private String name = "";
    private final int id;

    public MinSignature(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public static ArrayList<MinSignature> dbParse(ArrayList<HashMap> map) {
        return new ArrayList<>();
    }

    public static MinSignature reduceIntoMinSignature(Signature signature) {
        return new MinSignature(1);
    }

}
