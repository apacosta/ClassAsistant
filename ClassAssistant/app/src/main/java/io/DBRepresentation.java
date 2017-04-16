package io;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


/**
 * Created by asmateus on 5/04/17.
 */

public class DBRepresentation extends SQLiteOpenHelper {
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

    public static final int TYPE_NONE = -1;
    public static final int TYPE_STUDENT = 0;
    public static final int TYPE_SIGNATURE = 1;
    public static final int TYPE_EVALUATION = 2;
    public static final int TYPE_REPORT = 3;
    public static final int TYPE_RUBRIC_TEMPLATE = 4;
    public static final int TYPE_CATEGORY = 5;
    public static final int TYPE_RUBRIC = 6;
    public static final String DEFAULT_DB_NAME = "class_assistant";

    private static final int DATABASE_VERSION = 1;
    private int table_type = TYPE_NONE;

    public DBRepresentation(Context context) {
        super(context, DEFAULT_DB_NAME, null, DATABASE_VERSION);
    }

    public void setTableType(int type) {
        this.table_type = type;
    }

    public int getTableType() {
        return this.table_type;
    }


   // @Override
    public void onCreate(SQLiteDatabase db) {
        Integer[] tables = new Integer[] {
                DBRepresentation.TYPE_CATEGORY,
                DBRepresentation.TYPE_EVALUATION,
                DBRepresentation.TYPE_REPORT,
                DBRepresentation.TYPE_RUBRIC,
                DBRepresentation.TYPE_RUBRIC_TEMPLATE,
                DBRepresentation.TYPE_SIGNATURE,
                DBRepresentation.TYPE_STUDENT
        };

        for(Integer e: tables) {
            String cmd = "CREATE TABLE ";
            String table_name = "";
            String id = "";
            String params = "";

            switch(e) {
                case TYPE_STUDENT:
                    table_name = Student.TABLE_NAME;
                    id = Student._ID + " INTEGER PRIMARY KEY,";
                    params += Student.COLUMN_NAME + " TEXT,";
                    params += Student.COLUMN_CUSTOM_ID + " TEXT,";
                    params += Student.COLUMN_EVALUATIONS + " TEXT,";
                    params += Student.COLUMN_REPORTS + " TEXT,";
                    params += Student.COLUMN_SIGNATURES + " TEXT";
                    break;
                case TYPE_SIGNATURE:
                    table_name = Signature.TABLE_NAME;
                    id = Signature._ID + " INTEGER PRIMARY KEY,";
                    params += Signature.COLUMN_NAME + " TEXT,";
                    params += Signature.COLUMN_EVALUATIONS + " TEXT,";
                    params += Signature.COLUMN_GLOBAL_RUBRIC + " TEXT,";
                    params += Signature.COLUMN_REPORTS + " TEXT,";
                    params += Signature.COLUMN_STUDENTS + " TEXT";
                    break;
                case TYPE_EVALUATION:
                    table_name = Evaluation.TABLE_NAME;
                    id = Evaluation._ID + " INTEGER PRIMARY KEY,";
                    params += Evaluation.COLUMN_NAME + " TEXT,";
                    params += Evaluation.COLUMN_RESULTS_STUDENTS + " TEXT,";
                    params += Evaluation.COLUMN_RUBRIC + " TEXT";
                    break;
                case TYPE_RUBRIC_TEMPLATE:
                    table_name = RubricTemplate.TABLE_NAME;
                    id = RubricTemplate._ID + " INTEGER PRIMARY KEY,";
                    params += RubricTemplate.COLUMN_NAME + " TEXT,";
                    params += RubricTemplate.COLUMN_CATEGORIES + " TEXT";
                    break;
                case TYPE_REPORT:
                    table_name = Report.TABLE_NAME;
                    id = Report._ID + " INTEGER PRIMARY KEY,";
                    params += Report.COLUMN_CONTENT + " TEXT,";
                    params += Report.COLUMN_TARGET + " TEXT,";
                    params += Report.COLUMN_TYPE + " TEXT";
                    break;
                case TYPE_RUBRIC:
                    table_name = Rubric.TABLE_NAME;
                    id = Rubric._ID + " INTEGER PRIMARY KEY,";
                    params += Rubric.COLUMN_TEMPLATE + " TEXT,";
                    params += Rubric.COLUMN_WEIGHTS + " TEXT";
                    break;
                case TYPE_CATEGORY:
                    table_name = Categories.TABLE_NAME;
                    id = Categories._ID + " INTEGER PRIMARY KEY,";
                    params += Categories.COLUMN_ELEMENTS + " TEXT";
                    break;
                default:
                    return;
            }
            cmd += table_name + " (" + id + params + ")";

            db.execSQL(cmd);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Integer[] tables = new Integer[] {
                DBRepresentation.TYPE_CATEGORY,
                DBRepresentation.TYPE_EVALUATION,
                DBRepresentation.TYPE_REPORT,
                DBRepresentation.TYPE_RUBRIC,
                DBRepresentation.TYPE_RUBRIC_TEMPLATE,
                DBRepresentation.TYPE_SIGNATURE,
                DBRepresentation.TYPE_STUDENT
        };
        for(Integer e: tables) {
            String cmd = "DROP TABLE IF EXISTS ";
            switch(this.table_type) {
                case TYPE_STUDENT:
                    cmd += Student.TABLE_NAME;
                    break;
                case TYPE_SIGNATURE:
                    cmd += Signature.TABLE_NAME;
                    break;
                case TYPE_EVALUATION:
                    cmd += Evaluation.TABLE_NAME;
                    break;
                case TYPE_RUBRIC_TEMPLATE:
                    cmd += RubricTemplate.TABLE_NAME;
                    break;
                case TYPE_REPORT:
                    cmd += Report.TABLE_NAME;
                    break;
                case TYPE_RUBRIC:
                    cmd += Rubric.TABLE_NAME;
                    break;
                case TYPE_CATEGORY:
                    cmd += Categories.TABLE_NAME;
                    break;
                default:
                    return;
            }

            db.execSQL(cmd);
        }
        this.onCreate(db);
    }

    public static class Student implements BaseColumns {
        public static String TABLE_NAME = "students";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_CUSTOM_ID = "id_student";
        public static String COLUMN_SIGNATURES = "signatures_taken";
        public static String COLUMN_EVALUATIONS = "evaluations";
        public static String COLUMN_REPORTS = "reports";
    }

    public static class Signature implements BaseColumns {
        public static String TABLE_NAME = "signature";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_EVALUATIONS = "evaluations";
        public static String COLUMN_STUDENTS = "students";
        public static String COLUMN_REPORTS = "reports";
        public static String COLUMN_GLOBAL_RUBRIC = "global_rubric";
    }

    public static class Evaluation implements BaseColumns {
        public static String TABLE_NAME = "evaluation";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_RUBRIC = "rubric";
        public static String COLUMN_RESULTS_STUDENTS = "results_per_student";
    }

    public static class Report implements BaseColumns {
        public static String TABLE_NAME = "report";
        public static String COLUMN_TYPE = "type";
        public static String COLUMN_TARGET = "target";
        public static String COLUMN_CONTENT = "content";
    }

    public static class RubricTemplate implements BaseColumns {
        public static String TABLE_NAME = "rubric_template";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_CATEGORIES = "categories";
    }

    public static class Rubric implements BaseColumns {
        public static String TABLE_NAME = "rubric";
        public static String COLUMN_TEMPLATE = "rubric_template";
        public static String COLUMN_WEIGHTS = "rubric_weights";
    }

    public static class Categories implements BaseColumns {
        public static String TABLE_NAME = "categories";
        public static String COLUMN_ELEMENTS = "elements";
    }
}
