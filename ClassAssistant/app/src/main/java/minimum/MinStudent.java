package minimum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinStudent implements Serializable {

    private String name = "";

    public static ArrayList<MinStudent> dbParse(ArrayList<HashMap> map) {
        return new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
