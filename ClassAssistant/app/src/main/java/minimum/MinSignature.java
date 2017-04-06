package minimum;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinSignature {

    private String name = "";

    public MinSignature(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static ArrayList<MinSignature> dbParse(ArrayList<HashMap> map) {
        return new ArrayList<>();
    }

}
