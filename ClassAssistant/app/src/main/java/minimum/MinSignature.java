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
    private double score_average = 0;
    private int num_students = 0;
    private int num_students_below_avg = 0;
    private int num_students_above_avg = 0;
    private final long id;
    private String id_str = "";

    public MinSignature(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public double getScoreAverage() {
        return this.score_average;
    }

    public int getNumStudents() {
        return this.num_students;
    }

    public int getNumStudentsBelowAvg() {
        return this.num_students_below_avg;
    }

    public int getNumStudentsAboveAvg() {
        return this.num_students_above_avg;
    }

    public String getIdStr() {
        return id_str;
    }

    public void setIdStr(String id) {
        this.id_str = id;
    }

    public void setScoreAverage(double avg) {
        this.score_average = avg;
    }

    public void setNumStudents(int num) {
        this.num_students = num;
    }

    public void setNumStudentsBelowAvg(int ba) {
        this.num_students_below_avg = ba;
    }

    public void setNumStudentsAboveAvg(int aa) {
        this.num_students_above_avg = aa;
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
        MinSignature s = new MinSignature(signature.getID());
        s.setName(signature.getName());
        return s;
    }

    public static MinSignature fromExternalID(long id, MinSignature s) {
        MinSignature ss = new MinSignature(id);
        ss.setName(s.getName());

        return ss;
    }

}
