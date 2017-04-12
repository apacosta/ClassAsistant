package minimum;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinSignature {

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

}
