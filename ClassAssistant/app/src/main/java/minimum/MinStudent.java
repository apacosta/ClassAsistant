package minimum;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import io.DBRepresentation;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinStudent implements Serializable {

    private String name = "";
    private long id = 0;

    public MinStudent(long id) {

    }

    public static ArrayList<MinStudent> dbParse(ArrayList<HashMap> map) {
        ArrayList<MinStudent> s = new ArrayList<>();

        for(HashMap e: map) {

            try {
                MinStudent es = new MinStudent(Long.parseLong((String) e.get(DBRepresentation.Student._ID)));
                es.setName((String) e.get(DBRepresentation.Student.COLUMN_NAME));
                s.add(es);
            }
            catch(Exception f) {}
        }
        return s;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getID() {
        return this.id;
    }

    public void setID(String nam) {
        this.name = name;
    }

    public static MinStudent fromExternalID(long id, MinStudent s) {
        MinStudent ss = new MinStudent(id);
        ss.setName(s.getName());

        return ss;
    }
}
