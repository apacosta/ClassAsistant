package minimum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Signature;
import io.DBRepresentation;

/**
 * Created by asmateus on 5/04/17.
 */

public class MinSignature implements Serializable {

    private String name = "";
    private final long id;

    public MinSignature(long id) {
        this.id = id;
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

    public static ArrayList<MinSignature> dbParse(ArrayList<HashMap> map) {
        ArrayList<MinSignature> data = new ArrayList<>();
        MinSignature s;
        for(HashMap e: map) {
            String id = (String) e.get(DBRepresentation.Signature._ID);

            s = new MinSignature(Long.parseLong(id));
            s.setName((String) e.get(DBRepresentation.Signature.COLUMN_NAME));
            data.add(s);
        }
        return data;
    }

    public static MinSignature reduceIntoMinSignature(Signature signature) {
        return new MinSignature(1);
    }

    public static MinSignature fromExternalID(long id, MinSignature s) {
        MinSignature ss = new MinSignature(id);
        ss.setName(s.getName());

        return ss;
    }

}
