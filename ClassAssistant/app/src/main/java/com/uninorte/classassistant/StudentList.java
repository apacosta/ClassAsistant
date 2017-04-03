package com.uninorte.classassistant;

/**
 * Created by asmateus on 3/04/17.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentList {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> students = new ArrayList<String>();
        students.add("Lulu Lala");
        students.add("Omotori Koko");
        students.add("Misaka Mikoto");

        expandableListDetail.put("STUDENTS", students);
        return expandableListDetail;
    }
}