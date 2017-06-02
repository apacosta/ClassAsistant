package entities;

import java.util.ArrayList;

import minimum.MinEvaluation;
import minimum.MinStudent;

/**
 * Created by asmateus on 5/04/17.
 */

/*
    This class is not to be instantiated, and can only be created through expansion method
 */
public final class Signature {

    private final String id;

    private String name = "";
    private String default_rubric_id = "";
    private String signature_owner = "";

    private ArrayList<MinEvaluation> evaluations = new ArrayList<>();
    private ArrayList<MinStudent> students = new ArrayList<>();
    private ArrayList<MinStudent> petitions = new ArrayList<>();
    private ArrayList<String> available_rubrics_ids = new ArrayList<>();

    public Signature(String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }

    public void replaceEvaluation(MinEvaluation ev) {
        int i;
        boolean check = false;
        for(i = 0; i < evaluations.size(); ++i) {
            if(ev.getID().equals(evaluations.get(i).getID())) {
                check = true;
                break;
            }
        }
        if(check == true) {
            evaluations.remove(i);
        }
        evaluations.add(ev);
    }

    public void replaceStudent(MinStudent ev) {
        int i;
        boolean check = false;
        for(i = 0; i < students.size(); ++i) {
            if(ev.getID().equals(students.get(i).getID())) {
                check = true;
                break;
            }
        }
        if(check == true) {
            students.remove(i);
        }
        students.add(ev);
    }

    public void replacePetitionStudent(MinStudent ev) {
        int i;
        boolean check = false;
        for(i = 0; i < petitions.size(); ++i) {
            if(ev.getID().equals(petitions.get(i).getID())) {
                check = true;
                break;
            }
        }
        if(check == true) {
            petitions.remove(i);
        }
        petitions.add(ev);
    }

    public int getSumEvaluationWeights() {
        int sum = 0;
        for(MinEvaluation e: evaluations) {
            sum += e.getWeight();
        }

        return sum;
    }

    public String getName() {
        return this.name;
    }

    public String getDefaultRubricId() {
        return this.default_rubric_id;
    }

    public String getOwner() {
        return this.signature_owner;
    }

    public ArrayList<MinEvaluation> getEvaluations() {
        return this.evaluations;
    }

    public ArrayList<MinStudent> getStudents() {
        return this.students;
    }

    public ArrayList<MinStudent> getPetitions() {
        return this.petitions;
    }

    public ArrayList<String> getAvailableRubricIDs() {
        return this.available_rubrics_ids;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultRubricId(String dr) {
        this.default_rubric_id = dr;
    }

    public void setOwner(String owner) {
        this.signature_owner = owner;
    }

    public void setEvaluations(ArrayList<MinEvaluation> ev) {
        this.evaluations = ev;
    }

    public void setStudents(ArrayList<MinStudent> es) {
        this.students = es;
    }

    public void setPetitions(ArrayList<MinStudent> es) {
        this.petitions = es;
    }

    public void getAvailableRubricIDs(ArrayList<String> ru) {
        this.available_rubrics_ids = ru;
    }

}
