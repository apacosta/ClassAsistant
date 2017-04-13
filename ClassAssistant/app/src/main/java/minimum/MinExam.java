package minimum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinExam implements Serializable {

    private String name = "";
    private final int id;

    public MinExam(int id) {
        this.id = id;
    }

    public static ArrayList<MinExam> dbParse(ArrayList<HashMap> map) {
        return new ArrayList<>();
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

}
