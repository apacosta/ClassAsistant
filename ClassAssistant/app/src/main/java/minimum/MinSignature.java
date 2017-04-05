package minimum;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinSignature {

    private String title = "";

    public MinSignature(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public static ArrayList<MinSignature> dbParse(ArrayList<HashMap> map) {
        return new ArrayList<>();
    }

}
