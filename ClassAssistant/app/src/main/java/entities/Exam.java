package entities;

import minimum.MinExam;

/**
 * Created by asmateus on 5/04/17.
 */

public class Exam {

    private String name = "";
    private int id;

    private Exam() {}

    public String getName() {
        return this.name;
    }

    public static Exam expandIntoExam(MinExam minex) {
        Exam e = new Exam();
        e.name = minex.getName();
        e.id = minex.getID();

        return e;
    }
}
