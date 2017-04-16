package minimum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import io.DBRepresentation;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinExam implements Serializable {

    private String name = "";
    private final long id;

    public MinExam(long id) {
        this.id = id;
    }

    public static ArrayList<MinExam> dbParse(ArrayList<HashMap> map) {
        ArrayList<MinExam> s = new ArrayList<>();

        for(HashMap e: map) {
            try {
                MinExam es = new MinExam(Long.parseLong((String) e.get(DBRepresentation.Evaluation._ID)));
                es.setName((String) e.get(DBRepresentation.Evaluation.COLUMN_NAME));
                s.add(es);
            }
            catch(Exception f) {}
        }
        return s;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public long getID() {
        return this.id;
    }

    public static MinExam fromExternalID(long id, MinExam s) {
        MinExam ss = new MinExam(id);
        ss.setName(s.getName());

        return ss;
    }

}
