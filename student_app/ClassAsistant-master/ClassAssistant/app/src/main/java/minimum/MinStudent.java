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
    private String id = "";
    private String signatures = "";

    public MinStudent() {}


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }

    public String getSignatures() {
        return this.signatures;
    }
}
