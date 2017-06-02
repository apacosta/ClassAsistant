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
    private String students = "";
    private String evaluations = "";
    private String petitions = "";
    private String default_rubric = "";
    private String owner = "";
    private double score_average = 0;
    private int num_students = 0;
    private int num_students_below_avg = 0;
    private int num_students_above_avg = 0;
    private String id = "";

    public MinSignature() {
    }

    public void setStudents(String s) {
        this.students = s;
    }

    public String getStudents() {
        return this.students;
    }

    public void setEvaluations(String evaluations) {
        this.evaluations = evaluations;
    }

    public String getEvaluations() {
        return this.evaluations;
    }

    public void setPetitions(String petitions) {
        this.petitions = petitions;
    }

    public String getPetitions() {
        return this.petitions;
    }

    public void setDefaultRubric(String df) {
        this.default_rubric = df;
    }

    public String getDefaultRubric() {
        return this.default_rubric;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return this.owner;
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

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

}
