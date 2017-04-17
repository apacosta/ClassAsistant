package entities;

import java.util.ArrayList;
import java.util.List;

import minimum.MinExam;
import minimum.MinSignature;
import minimum.MinStudent;

/**
 * Created by asmateus on 5/04/17.
 */

/*
    This class is not to be instantiated, and can only be created through expansion method
 */
public final class Signature {

    private String name = "";
    private long id;
    private List<MinStudent> students = new ArrayList<>();
    private List<MinExam> evaluations = new ArrayList<>();

    private Signature(){}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public long getID() {
        return this.id;
    }

    public void addEvaluation(MinExam e) {
        this.evaluations.add(e);
    }

    public void addStudent(MinStudent s) {
        this.students.add(s);
    }

    public void destroyStudents() {
        this.students = new ArrayList<MinStudent>();
    }

    public void destroyEvaluations() {
        this.evaluations = new ArrayList<MinExam>();
    }

    public List<MinExam> getEvaluations() {
        return this.evaluations;
    }

    public List<MinStudent> getStudents() {
        return this.students;
    }

    public static Signature expandIntoSignature(MinSignature minsig) {
        Signature s = new Signature();
        s.name = minsig.getName();
        s.id = minsig.getID();

        return s;
    }
}
