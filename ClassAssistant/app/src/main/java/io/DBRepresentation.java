package io;

/**
 * Created by asmateus on 5/04/17.
 */

public class DBRepresentation {
    /*
    *
    * Database representation
    * Students:
    * ID_AUTO | NAME | ID STUDENT | SIGNATURES TAKEN (IDs) | EVALUATIONS (IDs) | REPORTS (IDs)
    * Signature:
    * ID_AUTO | NAME | EVALUATIONS (IDs) | STUDENTS (IDs) | REPORTS (IDs) | GLOBAL RUBRIC (ID)
    * Evaluation:
    * ID_AUTO | NAME | RUBRIC (ID) | RESULTS PER STUDENT
    * Reports:
    * ID_AUTO | TYPE | TARGET (IDs) | CONTENT
    * Rubric template:
    * ID_AUTO | NAME | CATEGORIES (IDs)
    * Categories:
    * ID_AUTO | ELEMENTS
    * Rubric:
    * ID_AUTO | RUBRIC TEMPLATE (IDs) | ASSIGNED WEIGHTS
    *
    */

    public static class Student {
        public static String TABLE_NAME = "students";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_CUSTOM_ID = "id_student";
        public static String COLUMN_SIGNATURES = "signatures_taken";
        public static String COLUMN_EVALUATIONS = "evaluations";
        public static String COLUMN_REPORTS = "reports";
    }

    public static class Signature {
        public static String TABLE_NAME = "signature";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_EVALUATIONS = "evaluations";
        public static String COLUMN_STUDENTS = "students";
        public static String COLUMN_REPORTS = "reports";
        public static String COLUMN_GLOBAL_RUBRIC = "global_rubric";
    }

    public static class Evaluation {
        public static String TABLE_NAME = "evaluation";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_RUBRIC = "rubric";
        public static String COLUMN_RESULTS_STUDENTS = "results_per_student";
    }

    public static class Report {
        public static String TABLE_NAME = "report";
        public static String COLUMN_TYPE = "type";
        public static String COLUMN_TARGET = "target";
        public static String COLUMN_CONTENT = "content";
    }

    public static class RubricTemplate {
        public static String TABLE_NAME = "rubric_template";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_CATEGORIES = "categories";
    }

    public static class Rubric {
        public static String TABLE_NAME = "rubric";
        public static String COLUMN_TEMPLATE = "rubric_template";
        public static String COLUMN_WEIGHTS = "rubric_weights";
    }

    public static class Categories {
        public static String TABLE_NAME = "categories";
        public static String COLUMN_ELEMENTS = "elements";
    }
}
